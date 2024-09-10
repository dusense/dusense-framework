package net.dusense.framework.core.abmodule;

import jakarta.ws.rs.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/***
 *
 * <p>Description: [基本实现service 接口类定义
 *  @RequestMapping 需要在facade 引用 Note:参数定义 必须具体化 不能参数 泛型化
 * ]</p>
 * @author:[Fan Yang]
 * @version $Revision$
 */
public interface BaseResource<VO> {

    @GetMapping(value = "/")
    public List<VO> list();

    @GetMapping(value = "/{id}")
    public VO query(@PathParam("id") String id);

    /** ids eg= 1,3,34 * */
    @GetMapping(value = "/batch/{ids}")
    public List<VO> queryBatch(@PathParam("ids") String ids);

    @DeleteMapping(value = "/{id}")
    public Boolean delete(@PathParam("id") String id);

    @PostMapping(value = "/")
    public VO create(VO model);

    @PostMapping(value = "/{id}")
    public VO update(@PathParam("id") String id, VO model);
}
