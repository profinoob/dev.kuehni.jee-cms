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

    @Nonnull
    public Page getRoot() {
        return pageRepository.findRoot();
    }

    @Nonnull
    public List<Page> getChildrenOf(@Nonnull Page parent) {
        return pageRepository.findChildren(parent);
    }

    @Nonnull
    public Optional<Page> getAtPath(@Nonnull PagePath path) {
        Objects.requireNonNull(path, "path");

        if (path.isRoot()) {
            return Optional.of(getRoot());
        }

        final var candidates = pageRepository.findBySlug(path.getLastSegment());
        return candidates.stream().filter(candidate -> pathEquals(candidate, path)).findAny();
    }

    @Nonnull
    public PagePath getPathOf(@Nonnull Page page) {
        Objects.requireNonNull(page, "page");

        final var builder = PagePath.builder();
        prependPathOf(page, builder);
        return builder.build();
    }

    private boolean pathEquals(@Nonnull Page page, @Nonnull PagePath path) {
        Objects.requireNonNull(path, "path");

        final var parent = page.getParent();
        if (parent == null) {
            return path.isRoot();
        }
        if (path.isRoot()) {
            return false;
        }

        if (!page.getSlug().equals(path.getLastSegment())) {
            return false;
        }

        return pathEquals(parent, path.withoutLastSegment());
    }

    private void prependPathOf(@Nonnull Page page, @Nonnull PagePath.Builder path) {
        final Page parent = page.getParent();
        if (parent == null) {
            return;
        }

        path.prepend(page.getSlug());
        prependPathOf(parent, path);
    }

    public void deleteRecursively(@Nonnull Page page) {
        Objects.requireNonNull(page, "page");

        if (page.getParent() == null) {
            return;
        }

        pageRepository.findChildren(page).forEach(this::deleteRecursively);
        pageRepository.delete(page.getId());
    }
}
