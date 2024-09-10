package net.dusense.framework.core.abmodule.genericcore.dao;

import net.dusense.framework.core.abmodule.BaseDao;
import net.dusense.framework.core.abmodule.genericcore.SearchParams;

import java.util.List;

/**
 * ${抽象Dao 基于spring-common-data 接口定义}
 *
 * @param <T>
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/11/09
 */
public interface GenericDao<T> extends BaseDao<T> {

    /**
     * 动态检索
     *
     * @param searchParams
     * @return
     */
    List<T> findBySearchParams(SearchParams searchParams);
}
