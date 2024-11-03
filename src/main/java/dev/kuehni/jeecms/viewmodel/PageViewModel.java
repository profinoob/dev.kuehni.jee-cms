package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import dev.kuehni.jeecms.util.text.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@Named
@RequestScoped
public class PageViewModel {
    @Nullable
    private Long id;
    @Nullable
    private Long parentId;

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
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable Long parentId) {
        this.parentId = parentId;
    }

    @Nullable
    public String getSlug() {
        return page.getSlug();
    }

    public void setSlug(@Nonnull String slug) {
        page.setSlug(slug);
    }

    @Nonnull
    public String getTitle() {
        return page.getTitle();
    }

    public void setTitle(@Nonnull String title) {
        page.setTitle(title);
    }

    @Nonnull
    public String getContent() {
        return page.getContent();
    }

    public void setContent(@Nonnull String content) {
        page.setContent(content);
    }


    @Nonnull
    public String getPath() {
        if (page.getParent() == null)
            return "/";

        final var parts = new ArrayList<String>();
        var current = page;
        while (current != null) {
            parts.add(current.getSlug());
            current = current.getParent();
        }
        return String.join("/", parts.reversed());
    }


    /// Load a page from {@link PageRepository} by the id set by {@link #setId(Long)}.
    public void load() {
        if (id != null) {
            pageRepository.findById(id).ifPresentOrElse(foundPage -> page = foundPage, () -> {
                final var facesContext = FacesContext.getCurrentInstance();
                final var response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page with id " + id + " not found");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                facesContext.responseComplete(); // Stops further processing
            });
        } else {
            page = new Page();
            if (parentId != null) {
                page.setParent(pageRepository.findById(parentId).orElse(null));
            }
        }
    }

    public void generateSlug() {
        final var title = getTitle();
        final var slug = StringUtils.stripAccents(title.toLowerCase())
                .replaceAll("[^a-z0-9\\-]+", "-")
                .replaceFirst("^-+", "")
                .replaceFirst("-+$", "")
                .replaceAll("-{2,}", "-");
        setSlug(slug);
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
