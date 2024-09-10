package net.dusense.framework.extension.datapermit.handler;

import lombok.RequiredArgsConstructor;
import net.dusense.framework.core.abmodule.genericcore.domain.AbstractBaseUser;
import net.dusense.framework.core.tool.utils.Func;
import net.dusense.framework.extension.datapermit.DataScopeModel;
import net.dusense.framework.extension.datapermit.constant.RoleConstant;
import net.dusense.framework.extension.datapermit.enums.DataScopeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 默认数据权限规则 */
@RequiredArgsConstructor
public class DusenseDataScopeHandler implements DataScopeHandler {

    private final ScopeModelHandler scopeModelHandler;

    @Override
    public String sqlCondition(
            String mapperId,
            DataScopeModel dataScope,
            AbstractBaseUser dusenseUser,
            String originalSql) {

        // 数据权限资源编号
        String code = dataScope.getResourceCode();

        // 根据mapperId从数据库中获取对应模型
        DataScopeModel dataScopeDb =
                scopeModelHandler.getDataScopeByMapper(mapperId, dusenseUser.getRoleId());

        // mapperId配置未取到则从数据库中根据资源编号获取
        if (dataScopeDb == null && StringUtils.isNotBlank(code)) {
            dataScopeDb = scopeModelHandler.getDataScopeByCode(code);
        }

        // 未从数据库找到对应配置则采用默认
        dataScope = (dataScopeDb != null) ? dataScopeDb : dataScope;

        // 判断数据权限类型并组装对应Sql
        Integer scopeRule = Objects.requireNonNull(dataScope).getScopeType();
        DataScopeEnum scopeTypeEnum = DataScopeEnum.of(scopeRule);
        List<Serializable> ids = new ArrayList<>();
        String whereSql = "where scope.{} in ({})";
        if (DataScopeEnum.ALL == scopeTypeEnum
                || StringUtils.containsAny(dusenseUser.getRoleName(), RoleConstant.ADMINISTRATOR)) {
            return null;
        } else if (DataScopeEnum.CUSTOM == scopeTypeEnum) {
            // @TODO 需要自定义处理范围
            // whereSql =
            // PlaceholderUtil.getDefaultResolver().resolveByMap(dataScope.getScopeValue(),
            // BeanUtils.toMap(dusenseUser));
        } else if (DataScopeEnum.OWN == scopeTypeEnum) {
            ids.add(dusenseUser.getUserId());
        } else if (DataScopeEnum.OWN_DEPT == scopeTypeEnum) {
            ids.addAll(Func.toLongList(dusenseUser.getDeptId()));
        } else if (DataScopeEnum.OWN_DEPT_CHILD == scopeTypeEnum) {
            List<Long> deptIds = Func.toLongList(dusenseUser.getDeptId());
            ids.addAll(deptIds);
            deptIds.forEach(
                    deptId -> {
                        List<Long> deptIdList = scopeModelHandler.getDeptAncestors(deptId);
                        ids.addAll(deptIdList);
                    });
        }
        return String.format(
                " select {} from ({}) scope " + whereSql,
                Func.toStr(dataScope.getScopeField(), "*"),
                originalSql,
                dataScope.getScopeColumn(),
                StringUtils.join(ids));
    }
}
