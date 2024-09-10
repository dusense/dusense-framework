package net.dusense.framework.extension.datapermit.handler;

import lombok.RequiredArgsConstructor;
import net.dusense.framework.extension.datapermit.DataScopeModel;
import net.dusense.framework.extension.datapermit.constant.DataScopeConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** DusenseScopeModelHandler */
@RequiredArgsConstructor
public class DusenseScopeModelHandler implements ScopeModelHandler {

    private static final String SCOPE_CACHE_CODE = "dataScope:code:";
    private static final String SCOPE_CACHE_CLASS = "dataScope:class:";
    private static final String DEPT_CACHE_ANCESTORS = "dept:ancestors:";
    private static final DataScopeModel SEARCHED_DATA_SCOPE_MODEL =
            new DataScopeModel(Boolean.TRUE);

    private final JdbcTemplate jdbcTemplate;

    /**
     * 获取数据权限
     *
     * @param mapperId 数据权限mapperId
     * @param roleId 用户角色集合
     * @return DataScopeModel
     */
    @Override
    public DataScopeModel getDataScopeByMapper(String mapperId, String roleId) {
        List<Object> args = new ArrayList<>(Collections.singletonList(mapperId));
        List<Long> roleIds = Collections.singletonList(Long.valueOf(roleId));
        args.addAll(roleIds);
        // 增加searched字段防止未配置的参数重复读库导致缓存击穿
        // 后续若有新增配置则会清空缓存重新加载
        DataScopeModel dataScope =
                null; // CacheUtil.get(SYS_CACHE, SCOPE_CACHE_CLASS, mapperId + StringPool.COLON +
        // roleId, DataScopeModel.class);
        if (dataScope == null || !dataScope.getSearched()) {
            List<DataScopeModel> list =
                    jdbcTemplate.query(
                            DataScopeConstant.dataByMapper(roleIds.size()),
                            args.toArray(),
                            new BeanPropertyRowMapper<>(DataScopeModel.class));
            //            if (Collections.isNotEmpty(list)) {
            //                dataScope = list.iterator().next();
            //                dataScope.setSearched(Boolean.TRUE);
            //            } else {
            //                dataScope = SEARCHED_DATA_SCOPE_MODEL;
            //            }
            //            CacheUtil.put(SYS_CACHE, SCOPE_CACHE_CLASS, mapperId + StringPool.COLON +
            // roleId, dataScope);
        }
        return StringUtils.isNotBlank(dataScope.getResourceCode()) ? dataScope : null;
    }

    /**
     * 获取数据权限
     *
     * @param code 数据权限资源编号
     * @return DataScopeModel
     */
    @Override
    public DataScopeModel getDataScopeByCode(String code) {
        DataScopeModel dataScope =
                null; // CacheUtil.get(SYS_CACHE, SCOPE_CACHE_CODE, code, DataScopeModel.class);
        // 增加searched字段防止未配置的参数重复读库导致缓存击穿
        // 后续若有新增配置则会清空缓存重新加载
        if (dataScope == null || !dataScope.getSearched()) {
            List<DataScopeModel> list =
                    jdbcTemplate.query(
                            DataScopeConstant.DATA_BY_CODE,
                            new Object[] {code},
                            new BeanPropertyRowMapper<>(DataScopeModel.class));
            //            if (Objects.nonNull(list)) {
            //                dataScope = list.iterator().next();
            //                dataScope.setSearched(Boolean.TRUE);
            //            } else {
            //                dataScope = SEARCHED_DATA_SCOPE_MODEL;
            //            }
            //            CacheUtil.put(SYS_CACHE, SCOPE_CACHE_CODE, code, dataScope);
        }
        return StringUtils.isNotBlank(dataScope.getResourceCode()) ? dataScope : null;
    }

    /**
     * 获取部门子级
     *
     * @param deptId 部门id
     * @return deptIds
     */
    @Override
    public List<Long> getDeptAncestors(Long deptId) {
        List ancestors =
                null; // CacheUtil.get(SYS_CACHE, DEPT_CACHE_ANCESTORS, deptId, List.class);
        //        if (CollectionUtil.isEmpty(ancestors)) {
        //            ancestors = jdbcTemplate.queryForList(DataScopeConstant.DATA_BY_DEPT, new
        // Object[]{deptId}, Long.class);
        //            CacheUtil.put(SYS_CACHE, DEPT_CACHE_ANCESTORS, deptId, ancestors);
        //        }
        return ancestors;
    }
}
