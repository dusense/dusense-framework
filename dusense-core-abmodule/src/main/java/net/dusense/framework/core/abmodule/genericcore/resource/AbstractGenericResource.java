package net.dusense.framework.core.abmodule.genericcore.resource;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.core.abmodule.BaseResource;
import net.dusense.framework.core.abmodule.BaseService;
import net.dusense.framework.core.web.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractGenericResource<VO, BServie extends BaseService<VO>>
        implements BaseResource<VO> {

    @Inject protected BServie service;

    @Override
    @GetMapping(value = "/")
    public List<VO> list() {
        return service.listAll();
    }

    @Override
    public VO query(String id) {
        return service.findById(id);
    }

    @Override
    public List<VO> queryBatch(String ids) {
        return service.findByIds(ids);
    }

    @Override
    public Boolean delete(String id) {
        return service.removeById(id);
    }

    @Override
    public VO create(VO vo) {
        return service.save(vo);
    }

    @Override
    public VO update(String id, VO vo) {
        return service.save(vo);
    }

    /**
     * @param query
     * @return @TODO VO 结果过滤, 当VO & DO 是同一个时,采用DO 过滤, 否则VO进行过滤
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public R<List<VO>> queryByPageSearchParams(
            @RequestParam(required = false) Map<String, Object> query) {
        // 区分service 基础CRUD Dao服务
        List<VO> result = service.queryBySearchParams(query);

        Long total = (Long) query.get("total");
        return R.ok(result).pageTotal(total);
    }
}
