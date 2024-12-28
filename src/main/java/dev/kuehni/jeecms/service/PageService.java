package dev.kuehni.jeecms.service;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.util.page.PagePath;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Named
@ApplicationScoped
public class PageService {
    @Inject
    private PageRepository pageRepository;

    /// Returns the index page (root of the hierarchy).
    @Nonnull
    public Page getRoot() {
        return pageRepository.findRoot();
    }

    /// Returns a list of all child pages of `parent`.
    @Nonnull
    public List<Page> getChildrenOf(@Nonnull Page parent) {
        return pageRepository.findChildren(parent);
    }

    /// Fetches the page with the given `path`, if it exists. Returns an empty optional otherwise.
    @Nonnull
    public Optional<Page> getAtPath(@Nonnull PagePath path) {
        Objects.requireNonNull(path, "path");

        if (path.isRoot()) {
            return Optional.of(getRoot());
        }

        final var candidates = pageRepository.findBySlug(path.getLastSegment());
        return candidates.stream().filter(candidate -> pathEquals(candidate, path)).findAny();
    }

    /// Build the [PagePath] of the given `page` by concatenating all url slugs of the page and its parents.
    @Nonnull
    public PagePath getPathOf(@Nonnull Page page) {
        Objects.requireNonNull(page, "page");

        final var builder = PagePath.builder();
        prependPathOf(page, builder);
        return builder.build();
    }

    /// Whether the given `page` is located at the given `path`.
    private boolean pathEquals(@Nonnull Page page, @Nonnull PagePath path) {
        Objects.requireNonNull(path, "path");

        final var parent = page.getParent();
        if (parent == null) {
            // The page is the root. Therefore, the path must also represent the root.
            return path.isRoot();
        }
        if (path.isRoot()) {
            // The pate is not the root. There, the path shouldn't represent the root.
            return false;
        }

        if (!page.getSlug().equals(path.getLastSegment())) {
            // When the last segment of the path doesn't match the URL slug of the page, then the path cannot represent
            // the location of the page.
            return false;
        }

        // Removing the last segment of the path should yield a path that represents the parent of this page.
        return pathEquals(parent, path.withoutLastSegment());
    }

    /// Prepend the slug of the `page` as a segment to the `path`.
    private void prependPathOf(@Nonnull Page page, @Nonnull PagePath.Builder path) {
        final Page parent = page.getParent();
        if (parent == null) {
            return;
        }

        path.prepend(page.getSlug());
        prependPathOf(parent, path);
    }

    /// Delete the given `page` and all child pages recursively.
    public void deleteRecursively(@Nonnull Page page) {
        Objects.requireNonNull(page, "page");

        if (page.getParent() == null) {
            return;
        }

        pageRepository.findChildren(page).forEach(this::deleteRecursively);
        pageRepository.delete(page.getId());
    }
}
