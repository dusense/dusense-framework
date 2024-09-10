package net.dusense.framework.extension.datapermit.support;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/09/25
 */
@Slf4j
public class DataPermissionInterceptor implements StatementInspector {
    private String orgId;

    private List<String> orgIds;

    private List<String> orgTables;

    private String orgIdColumn = "group_id";

    @Override
    public String inspect(String sql) {
        try {

            orgIds = List.of("1");

            log.info("组织筛选解析开始，原始SQL：{}", sql);
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            StringBuilder sqlStringBuilder = new StringBuilder();
            int i = 0;
            for (Statement statement : statements.getStatements()) {
                if (null != statement) {
                    if (i++ > 0) {
                        sqlStringBuilder.append(';');
                    }
                    sqlStringBuilder.append(this.processParser(statement));
                }
            }
            String newSql = sqlStringBuilder.toString();
            log.info("组织筛选解析结束，解析后SQL：{}", newSql);
            return newSql;
        } catch (Exception e) {
            log.error("组织筛选解析失败，解析SQL异常{}", e.getMessage());
            e.printStackTrace();
        } finally {
            orgId = null;
        }
        return null;
    }

    private String processParser(Statement statement) {
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement);
        } else if (statement instanceof Select) {
            this.processSelectBody(((Select) statement).getSelectBody());
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement);
        }
        /** 返回处理后的SQL */
        return statement.toString();
    }

    /** select 语句处理 */
    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSubSelect() != null) {
                processSelectBody(withItem.getSubSelect().getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                operationList.getSelects().forEach(this::processSelectBody);
            }
        }
    }

    public void processInsert(Insert insert) {
        if (orgTables.contains(insert.getTable().getFullyQualifiedName())) {
            insert.getColumns().add(new Column(orgIdColumn));
            if (insert.getSelect() != null) {
                SelectBody selectBody = insert.getSelect().getSelectBody();
                if (selectBody instanceof PlainSelect) {
                    processPlainSelect((PlainSelect) selectBody, true);
                } else {
                    throw new RuntimeException(
                            "Unsupported select body type: " + selectBody.getClass());
                }
            } else if (insert.getColumns() instanceof ExpressionList) {
                ExpressionList expressionList = (ExpressionList) insert.getColumns();
                expressionList.getExpressions().add(new StringValue(orgId));
            } else {
                throw new RuntimeException(
                        "Failed to process multiple-table update, please exclude the tableName or statementId");
            }
        }
    }

    /** update 语句处理 */
    public void processUpdate(net.sf.jsqlparser.statement.update.Update update) {
        final Table table = update.getTable();
        if (orgTables.contains(table.getFullyQualifiedName())) {
            update.setWhere(this.andExpression(table, update.getWhere()));
        }
    }

    /** delete 语句处理 */
    public void processDelete(Delete delete) {
        if (orgTables.contains(delete.getTable().getFullyQualifiedName())) {
            delete.setWhere(this.andExpression(delete.getTable(), delete.getWhere()));
        }
    }

    /** delete update 语句 where 处理 */
    protected BinaryExpression andExpression(Table table, Expression where) {
        // 获得where条件表达式
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(this.getAliasColumn(table));
        equalsTo.setRightExpression(new StringValue(orgId));
        if (null != where) {
            if (where instanceof OrExpression) {
                return new AndExpression(equalsTo, new Parenthesis(where));
            } else {
                return new AndExpression(equalsTo, where);
            }
        }
        return equalsTo;
    }

    /** 处理 PlainSelect */
    protected void processPlainSelect(PlainSelect plainSelect) {
        if (plainSelect.getWhere() != null) {
            processPlainSelect(plainSelect, true);
        } else {
            processPlainSelect(plainSelect, false);
        }
    }

    /**
     * 处理 PlainSelect
     *
     * @param plainSelect ignore
     * @param addColumn 是否添加租户列,insert into select语句中需要
     */
    protected void processPlainSelect(PlainSelect plainSelect, boolean addColumn) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            if (orgTables.contains(fromTable.getFullyQualifiedName())) {
                // #1186 github
                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));
                if (addColumn) {
                    plainSelect
                            .getSelectItems()
                            .add(new SelectExpressionItem(new Column(orgIdColumn)));
                }
            }
        } else {
            processFromItem(fromItem);
        }
        List<Join> joins = plainSelect.getJoins();
        if (joins != null && joins.size() > 0) {
            joins.forEach(
                    j -> {
                        processJoin(j);
                        processFromItem(j.getRightItem());
                    });
        }
    }

    /** 处理子查询等 */
    protected void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoinList() != null) {
                subJoin.getJoinList().forEach(this::processJoin);
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            log.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    /** 处理联接语句 */
    protected void processJoin(Join join) {
        if (join.getRightItem() instanceof Table) {
            Table fromTable = (Table) join.getRightItem();
            if (orgTables.contains(fromTable.getFullyQualifiedName())) {
                join.setOnExpression(builderExpression(join.getOnExpression(), fromTable));
            }
        }
    }

    /** 处理条件: 创建InExpression，即封装where orgId in ('','') */
    protected Expression builderExpression(Expression currentExpression, Table table) {
        final InExpression organizationExpression = new InExpression();
        List<Expression> expressions = new ArrayList<>();
        orgIds.forEach(
                organizatinId -> {
                    expressions.add(new StringValue(organizatinId));
                });
        ExpressionList expressionList = new ExpressionList(expressions);
        organizationExpression.setLeftExpression(this.getAliasColumn(table));
        organizationExpression.setRightItemsList(expressionList);

        Expression appendExpression = null;
        if (!(organizationExpression instanceof SupportsOldOracleJoinSyntax)) {
            appendExpression = new EqualsTo();
            ((EqualsTo) appendExpression).setLeftExpression(this.getAliasColumn(table));
            ((EqualsTo) appendExpression).setRightExpression(organizationExpression);
        }
        if (currentExpression == null) {
            return organizationExpression;
        } else {
            appendExpression = organizationExpression;
        }
        if (currentExpression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
            doExpression(binaryExpression.getLeftExpression());
            doExpression(binaryExpression.getRightExpression());
        } else if (currentExpression instanceof InExpression) {
            InExpression inExp = (InExpression) currentExpression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), appendExpression);
        } else {
            return new AndExpression(currentExpression, appendExpression);
        }
    }

    protected void doExpression(Expression expression) {
        if (expression instanceof FromItem) {
            processFromItem((FromItem) expression);
        } else if (expression instanceof InExpression) {
            InExpression inExp = (InExpression) expression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
    }

    /**
     * 租户字段别名设置
     *
     * <p>tableName.orgId 或 tableAlias.orgId
     *
     * @param table 表对象
     * @return 字段
     */
    protected Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        if (null == table.getAlias()) {
            column.append(table.getName());
        } else {
            column.append(table.getAlias().getName());
        }
        column.append(".");
        column.append(orgIdColumn);
        return new Column(column.toString());
    }
}
