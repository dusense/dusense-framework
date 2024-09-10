package net.dusense.framework.core.abmodule;

import java.io.Serializable;

/**
 * ${基础Dao 基于spring-common-data 接口定义}
 *
 * @param <T>
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/11/09
 */
public interface BaseDao<T> extends BaseCrudRepository<T, Serializable>
/*extends PagingAndSortingRepository<T, Serializable>, CrudRepository<T, Serializable> */ {

    Iterable<T> findAll(Page page);
}
