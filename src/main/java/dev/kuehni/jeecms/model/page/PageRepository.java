package dev.kuehni.jeecms.model.page;

import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class PageRepository extends CrudRepository<Page, Long> {
    public PageRepository() {
        super(Page.class);
    }

    @Nonnull
    private TypedQuery<Page> queryRootPage() {
        return getEntityManager().createQuery("select p from Page p where p.parent is null", Page.class);
    }

    @Transactional
    public void createRootPageIfNotExists() {
        try {
            queryRootPage().getSingleResult();
        } catch (NoResultException ex) {
            final Page rootPage = new Page();
            rootPage.setTitle("Homepage");
            rootPage.setSlug("-");
            insert(rootPage);
        }
    }

    @Nonnull
    public Page findRoot() {
        return queryRootPage().getSingleResult();
    }

    public boolean existsChildrenOf(@Nonnull final Page parent) {
        return !queryChildrenOf(parent).setMaxResults(1).getResultList().isEmpty();
    }

    @Nonnull
    public List<Page> findChildren(@Nonnull final Page parent) {
        return queryChildrenOf(parent).getResultList();
    }

    @Nonnull
    private TypedQuery<Page> queryChildrenOf(@Nonnull Page parent) {
        return getEntityManager()
                .createQuery("select p from Page p where p.parent = :parent order by p.slug", Page.class)
                .setParameter("parent", parent);
    }

    @Nonnull
    public List<Page> findBySlug(@Nonnull final String slug) {
        Objects.requireNonNull(slug, "slug");

        return getEntityManager()
                .createQuery("select p from Page p where p.slug = :slug", Page.class)
                .setParameter("slug", slug)
                .getResultList();
    }
}
