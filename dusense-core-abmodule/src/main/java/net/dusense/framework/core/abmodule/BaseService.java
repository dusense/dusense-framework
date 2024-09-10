package net.dusense.framework.core.abmodule;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/***
 *
 * <p>Description: [基本实现service 接口类定义
 *  @RequestMapping 需要在facade 引用 Note:参数定义 必须具体化 不能参数 泛型化
 * ]</p>
 * @author:[Fan Yang]
 * @version $Revision$
 */
public interface BaseService<BO> {

    /**
     * 获取所有
     *
     * @return
     */
    List<BO> listAll();

    /**
     * 根据ID查找
     *
     * @param id
     * @return
     */
    BO findById(Serializable id);

    /**
     * 批量获取相关Ids ids eg= 1,3,34
     *
     * @param ids
     * @return
     */
    List<BO> findByIds(String ids);

    /**
     * 新增
     *
     * @param boModel
     * @return
     */
    BO save(BO boModel);

    BO updateById(Serializable id, BO boModel);

    Boolean removeById(Serializable id);

    public List<BO> queryBySearchParams(Map<String, Object> query);
}
