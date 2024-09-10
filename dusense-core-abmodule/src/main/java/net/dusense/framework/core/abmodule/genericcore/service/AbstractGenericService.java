package net.dusense.framework.core.abmodule.genericcore.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.core.abmodule.BaseService;
import net.dusense.framework.core.abmodule.Page;
import net.dusense.framework.core.abmodule.genericcore.SearchParams;
import net.dusense.framework.core.abmodule.genericcore.convert.Convertable;
import net.dusense.framework.core.abmodule.genericcore.dao.GenericDao;
import net.dusense.framework.core.web.R;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractGenericService<BO, DAO extends GenericDao>
        implements BaseService<BO> {

    @Inject protected DAO dao;
    //    @Inject
    protected Convertable convertable;

    /**
     * Discription:[方法功能中文描述]
     *
     * @return
     * @author:[Fan Yang]
     * @update:[日期2018-01-11] [更改人姓名][变更描述]
     */
    @Override
    public List<BO> listAll() {
        // list All
        Iterable<?> doFlux = dao.findAll(Page.ofSize(10));
        List<BO> voFlux = null;

        Object bo = null;
        if (Objects.isNull(convertable)) {
            // 不需要处理转换
            voFlux = (List<BO>) doFlux;
        } else {
            // 引用对象处理转换
            List<BO> eachVOFlux = voFlux = Lists.newArrayList();
            doFlux.forEach(
                    project -> {
                        BO vo = (BO) convertable.describe(project);
                        eachVOFlux.add(vo);
                    });
        }

        return voFlux;
    }

    @Override
    public BO findById(@PathVariable("id") Serializable id) {
        Object doMono = dao.findById(id).orElse(null);
        BO bo = null;
        if (Objects.isNull(convertable)) {
            bo = (BO) doMono;
        } else {
            bo = (BO) convertable.describe(doMono);
        }
        return bo;
    }

    @Override
    public List<BO> findByIds(@PathVariable("ids") String ids) {
        List idArray = Splitter.on(",").splitToList(ids);

        Iterable<?> doFlux = dao.findAllById(idArray);
        List<BO> voFlux = null;

        Object bo = null;
        if (Objects.isNull(convertable)) {
            // 不需要处理转换
            voFlux = (List<BO>) doFlux;
        } else {
            // 引用对象处理转换
            List<BO> eachVOFlux = voFlux = Lists.newArrayList();
            doFlux.forEach(
                    project -> {
                        BO vo = (BO) convertable.describe(project);
                        eachVOFlux.add(vo);
                    });
        }

        return voFlux;
    }

    @Override
    public Boolean removeById(Serializable id) {
        dao.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public BO save(@RequestBody BO vo) {
        Object bo = null;
        if (Objects.isNull(convertable)) {
            bo = vo;
        } else {
            bo = convertable.convert(vo);
        }
        Object newDo = dao.save(bo);

        return vo;
    }

    @Override
    public BO updateById(Serializable id, BO vo) {
        Object bo = null;
        if (Objects.isNull(convertable)) {
            bo = vo;
        } else {
            bo = convertable.convert(vo);
        }

        Object newmodel = dao.save(bo);
        // @TODO 转回来
        return vo;
    }

    /**
     * @param query
     * @return @TODO VO 结果过滤, 当VO & DO 是同一个时,采用DO 过滤, 否则VO进行过滤
     */
    public R<List<BO>> queryByPageSearchParams(Map<String, Object> query) {
        // 区分service 基础CRUD Dao服务
        List<BO> result = this.queryBySearchParams(query);

        Long total = (Long) query.get("total");
        return R.ok(result).pageTotal(total);
    }

    /**
     * @param query
     * @return @TODO VO 结果过滤, 当VO & DO 是同一个时,采用DO 过滤, 否则VO进行过滤
     */
    public List<BO> queryBySearchParams(Map<String, Object> query) {
        // 区分service 基础CRUD Dao服务

        try {
            log.info("list thread is: {}", Thread.currentThread());
            SearchParams searchParams = new SearchParams(query);
            //            if (query.get("page") != null) {
            //                // 开启分页
            //                page = PageHelper.startPage(searchParams.getPage(),
            // searchParams.getLimit());
            //            } else {
            //                // 默认分页
            //                page = PageHelper.startPage(searchParams.getPage(),
            // searchParams.getLimit());
            //            }
            List<?> doFlux = dao.findBySearchParams(searchParams);

            List<BO> voFlux = null;
            Object bo = null;
            if (Objects.isNull(convertable)) {
                // 不需要处理转换
                voFlux = (List<BO>) doFlux;
            } else {
                // 引用对象处理转换
                voFlux =
                        doFlux.stream()
                                .map(
                                        project -> {
                                            BO vo = (BO) convertable.describe(project);
                                            return vo;
                                        })
                                .collect(Collectors.toList());
            }

            log.info("list thread is voFlux end: {}", Thread.currentThread());
            Long total = (Long) query.get("total");
            return voFlux;

        } finally {
            // query对象引用 用于回调查询结果

        }
    }
}
