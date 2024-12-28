package dev.kuehni.jeecms.util.data;


import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/// A repository with the basic create, read, update, and delete operations.
public abstract class CrudRepository<E, I> {
    @PersistenceContext
    private EntityManager em;

    private final Class<E> entityClass;

    /// Create a new repository for the given `entityClass`.
    public CrudRepository(@Nonnull Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    /// Persist the given `entity` as a new value.
    @Transactional
    public void insert(@Nonnull E entity) {
        Objects.requireNonNull(entity, "entity");
        em.persist(entity);
    }

    /// Retrieve an entity by its `id`. Returns an empty optional, if no such entity exists.
    @Nonnull
    public Optional<E> findById(@Nonnull I id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(em.find(entityClass, id));
    }

    /// Get a list of all persisted entities.
    @Nonnull
    public List<E> findAll() {
        return em.createQuery("select e from " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    /// Update an already persisted entity.
    @Transactional
    public void update(@Nonnull E entity) {
        Objects.requireNonNull(entity, "entity");
        em.merge(entity);
    }

    /// Remove the entity associated with the given `id`.
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
