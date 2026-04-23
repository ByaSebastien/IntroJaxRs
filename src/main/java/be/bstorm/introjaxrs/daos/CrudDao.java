package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.utils.EmfFactory;
import jakarta.persistence.EntityManagerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class CrudDao<TEntity, TId> {

    protected final EntityManagerFactory emf;
    private final Class<TEntity> entityClass;

    @SuppressWarnings("unchecked")
    public CrudDao() {
        this.emf = EmfFactory.getEmf();
        this.entityClass = (Class<TEntity>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }

    public List<TEntity> findAll() {
        try(var em = emf.createEntityManager()) {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
        }
    }

    public Optional<TEntity> findById(TId id) {
        try(var em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(entityClass, id));
        }
    }

    public TEntity save(TEntity entity) {
        try(var em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        }
    }

    public TEntity update(TEntity entity) {
        try(var em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            TEntity merged = em.merge(entity);
            tx.commit();
            return merged;
        }
    }

    public TEntity delete(TId id) {
        try(var em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            TEntity ref = em.getReference(entityClass,id);
            em.remove(ref);
            tx.commit();
            return ref;
        }
    }

    public boolean existsById(TId id) {
        try(var em = emf.createEntityManager()) {
            return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult() > 0;
        }
    }

    public long count() {
        try(var em = emf.createEntityManager()) {
            return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                    .getSingleResult();
        }
    }
}
