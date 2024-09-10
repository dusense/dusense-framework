package net.dusense.framework.extension.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.dusense.framework.core.abmodule.Page;
import net.dusense.framework.core.abmodule.genericcore.SearchParams;
import net.dusense.framework.core.abmodule.genericcore.dao.GenericDao;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Description: [Redis 泛型Dao]
 *
 * @author [ Fan Yang ]
 * @version $1.0$
 */
@NoRepositoryBean
public abstract class HibernateGenericDao<DO> implements GenericDao<DO> {

    @PersistenceContext EntityManager em;
    protected Class<DO> persistentClass;

    public HibernateGenericDao() {
        this.persistentClass = getEntityClass();
    }

    @Override
    public <S extends DO> S save(S entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <S extends DO> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<DO> findById(Serializable id) {
        return Optional.ofNullable(em.find(persistentClass, id));
    }

    @Override
    public boolean existsById(Serializable id) {
        return findById(id).isEmpty();
    }

    @Override
    public Iterable<DO> findAll() {

        //        em.getEntityManagerFactory().getCache().evictAll();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DO> cq = cb.createQuery(persistentClass);
        Root<DO> rootEntry = cq.from(persistentClass);
        CriteriaQuery<DO> all = cq.select(rootEntry);
        TypedQuery<DO> allQuery = em.createQuery(all);
        return allQuery.getResultList();
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
    public void deleteById(Serializable id) {
        delete(em.find(persistentClass, id));
    }

    @Override
    public void delete(DO entity) {
        if (Objects.nonNull(entity)) {
            em.remove(entity);
        }
    }

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DO> cq = cb.createQuery(persistentClass);
        Root<DO> rootEntry = cq.from(persistentClass);
        CriteriaQuery<DO> all = cq.select(rootEntry);

        Predicate predicate = buildPredicate(cb, rootEntry, searchParams);
        if (predicate != null) {
            all.where(predicate);
        }

        TypedQuery<DO> typedQuery = em.createQuery(all);
        typedQuery.setFirstResult((searchParams.getPage() - 1) * searchParams.getLimit());
        typedQuery.setMaxResults(searchParams.getLimit());
        List<DO> list = typedQuery.getResultList();

        return list;
    }

    public long countBySearchParams(final SearchParams searchParams) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(getEntityClass())));

        Predicate predicate = buildPredicate(cb, cq.from(getEntityClass()), searchParams);
        if (predicate != null) {
            cq.where(predicate);
        }

        return em.createQuery(cq).getSingleResult();
    }

    /**
     * Helper method to construct Predicate
     *
     * @param cb
     * @param root
     * @param searchParams
     * @return
     */
    private Predicate buildPredicate(
            CriteriaBuilder cb, Root<DO> root, final SearchParams searchParams) {
        if (searchParams == null || searchParams.isEmpty()) {
            return null;
        }

        //        searchParams.getOrderbyasc()

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            predicates.add(cb.equal(root.get(key), value));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    protected Class<DO> getEntityClass() {
        // 获取实体类类型
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<DO>) parameterizedType.getActualTypeArguments()[0];
    }
}
