package dev.kuehni.jeecms.page.viewmodel;

import dev.kuehni.jeecms.page.data.Page;
import dev.kuehni.jeecms.page.data.PageRepository;
import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;

@Named
@RequestScoped
public class PageViewModel {
    @Nullable
    private Long id;

    @Nonnull
    private Page page = new Page();

    @Inject
    private PageRepository pageRepository;
    @Inject
    private PrettyFacesRedirector prettyFacesRedirector;

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @Nullable
    public String getSlug() {
        return page.getSlug();
    }

    public void setSlug(@Nonnull String slug) {
        page.setSlug(slug);
    }

    @Nullable
    public String getTitle() {
        return page.getTitle();
    }

    public void setTitle(@Nonnull String title) {
        page.setTitle(title);
    }

    @Nullable
    public String getContent() {
        return page.getContent();
    }

    public void setContent(@Nonnull String content) {
        page.setContent(content);
    }


    /// Load a page from {@link PageRepository} by the id set by {@link #setId(Long)}.
    @Nullable
    public String load() {
        final var foundPage = Optional.ofNullable(id).flatMap(pageRepository::findById);
        page = foundPage.orElseGet(Page::new);
        return foundPage.isPresent() ? null : "Page not found";
    }

    /// Create a new page with data set to this view model and redirect to the view page for the newly created page.
    public void create() {
        pageRepository.insert(page);
        id = page.getId();

        prettyFacesRedirector.redirect("pretty:viewPage", id);
    }

    /// Delete this page and navigate to the page list.
    public void delete() {
        if (id != null) {
            pageRepository.delete(id);
        }
        prettyFacesRedirector.redirect("pretty:home");
    }
}
