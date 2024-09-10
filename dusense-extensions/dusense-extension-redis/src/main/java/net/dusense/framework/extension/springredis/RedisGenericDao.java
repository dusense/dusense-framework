package net.dusense.framework.extension.springredis;

import jakarta.inject.Inject;
import net.dusense.framework.core.abmodule.Page;
import net.dusense.framework.core.abmodule.genericcore.SearchParams;
import net.dusense.framework.core.abmodule.genericcore.dao.GenericDao;
import org.springframework.data.repository.NoRepositoryBean;

import javax.cache.CacheManager;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * Description: [Redis 泛型Dao]
 *
 * @author [ Fan Yang ]
 * @version $1.0$
 */
@NoRepositoryBean
public abstract class RedisGenericDao<DO> implements GenericDao<DO> {

    @Inject protected CacheManager cacheManager;
    protected Class<DO> persistentClass;

    public RedisGenericDao() {
        this.persistentClass = getEntityClass();
    }

    @Override
    public <S extends DO> S save(S entity) {
        return entity;
    }

    @Override
    public <S extends DO> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<DO> findById(Serializable id) {
        return null;
    }

    @Override
    public boolean existsById(Serializable id) {
        return findById(id).isEmpty();
    }

    @Override
    public Iterable<DO> findAll() {
        return null;
    }

    @Override
    public Iterable<DO> findAllById(Iterable<Serializable> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Serializable id) {}

    @Override
    public void delete(DO entity) {}

    @Override
    public void deleteAllById(Iterable<? extends Serializable> ids) {}

    @Override
    public void deleteAll(Iterable<? extends DO> entities) {}

    @Override
    public void deleteAll() {}

    public Iterable<DO> findAll(Page pageable) {
        //        return findBySearchParams(new SearchParams());
        return findAll();
    }

    @Override
    public List<DO> findBySearchParams(final SearchParams searchParams) {
        return null;
    }

    protected Class<DO> getEntityClass() {
        // 获取实体类类型
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<DO>) parameterizedType.getActualTypeArguments()[0];
    }

    public void exprire(String key, long timeout) {}
}
