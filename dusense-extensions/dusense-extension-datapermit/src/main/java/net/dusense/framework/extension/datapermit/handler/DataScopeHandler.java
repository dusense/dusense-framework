package net.dusense.framework.extension.datapermit.handler;

import net.dusense.framework.core.abmodule.genericcore.domain.AbstractBaseUser;
import net.dusense.framework.extension.datapermit.DataScopeModel;

/** 数据权限规则 */
public interface DataScopeHandler {

    /**
     * 获取过滤sql
     *
     * @param mapperId 数据查询类
     * @param dataScope 数据权限类
     * @param baseUser 当前用户信息
     * @param originalSql 原始Sql
     * @return sql
     */
    String sqlCondition(
            String mapperId,
            DataScopeModel dataScope,
            AbstractBaseUser baseUser,
            String originalSql);
}
