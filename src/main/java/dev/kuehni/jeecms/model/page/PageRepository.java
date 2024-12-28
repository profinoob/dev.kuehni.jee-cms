package dev.kuehni.jeecms.model.page;

import dev.kuehni.jeecms.config.page.Initialization;
import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
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

    /// Creates the root (or index) page.\
    /// This method is only intended to be called by [Initialization].
    ///
    /// @throws IllegalStateException if the root page (a page without parent) exists already.
    @Transactional
    public void createRootPage() {
        if (!queryRootPage().setMaxResults(1).getResultList().isEmpty()) {
            throw new IllegalStateException("Root page already exists");
        }

        final Page rootPage = new Page();
        rootPage.setTitle("Homepage");
        rootPage.setSlug("-");
        insert(rootPage);
    }

    /// Returns the index page (root of the hierarchy).
    @Nonnull
    public Page findRoot() {
        return queryRootPage().getSingleResult();
    }

    /// Returns `true` if the given page (`parent`) has any child page.
    public boolean existsChildrenOf(@Nonnull final Page parent) {
        return !queryChildrenOf(parent).setMaxResults(1).getResultList().isEmpty();
    }

    /// Returns a list of all child pages of `parent`.
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

    /// Finds all pages that have the given `slug` (URL segment).
    @Nonnull
    public List<Page> findBySlug(@Nonnull final String slug) {
        Objects.requireNonNull(slug, "slug");

        return getEntityManager()
                .createQuery("select p from Page p where p.slug = :slug", Page.class)
                .setParameter("slug", slug)
                .getResultList();
    }
}
