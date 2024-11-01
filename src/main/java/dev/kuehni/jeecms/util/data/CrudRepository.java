package dev.kuehni.jeecms.util.data;


import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class CrudRepository<E, I> {
    @PersistenceContext
    private EntityManager em;

    private final Class<E> entityClass;

    public CrudRepository(@Nonnull Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    public void insert(@Nonnull E entity) {
        Objects.requireNonNull(entity, "entity");
        em.persist(entity);
    }

    @Nonnull
    public Optional<E> findById(@Nonnull I id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Nonnull
    public List<E> findAll() {
        return em.createQuery("select e from " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    @Transactional
    public void update(@Nonnull E entity) {
        Objects.requireNonNull(entity, "entity");
        em.merge(entity);
    }

    @Transactional
    public void delete(@Nonnull I id) {
        Objects.requireNonNull(id, "id");
        findById(id).ifPresent(em::remove);
    }

    @Nonnull
    protected EntityManager getEntityManager() {
        return em;
    }
}
