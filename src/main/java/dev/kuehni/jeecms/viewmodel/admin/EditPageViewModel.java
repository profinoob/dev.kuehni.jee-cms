package dev.kuehni.jeecms.viewmodel.admin;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.service.PageService;
import dev.kuehni.jeecms.service.auth.PermissionService;
import dev.kuehni.jeecms.util.faces.FacesUtils;
import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import dev.kuehni.jeecms.util.text.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

@Named
@RequestScoped
public class EditPageViewModel {
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
    @Named
    @Inject
    private PageService pageService;
    @Inject
    private PermissionService permissionService;

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

    public boolean isNew() {
        return id == null;
    }


    @Nonnull
    public String getParentPath() {
        final var parts = new ArrayList<String>();
        var current = page.getParent();
        if (current == null || current.getParent() == null) {
            return "";
        }
        while (true) {
            final var parent = current.getParent();
            if (parent == null) break;
            parts.add(current.getSlug());
            current = parent;
        }
        return "/" + String.join("/", parts.reversed());
    }

    @Nonnull
    public String getPath() {
        final var parts = new ArrayList<String>();
        var current = page;
        while (true) {
            final var parent = current.getParent();
            if (parent == null) break;
            parts.add(current.getSlug());
            current = parent;
        }
        return "/" + String.join("/", parts.reversed());
    }


    /// Load a page from {@link PageRepository} by the id set by {@link #setId(Long)}.
    public void load() {
        if (!permissionService.isAllowedToEditPage()) {
            FacesUtils.respondWithError(HttpServletResponse.SC_FORBIDDEN);
        }

        if (id != null) {
            loadExisting(id);
        } else {
            loadNew();
        }
    }

    private void loadNew() {
        page = new Page();
        if (parentId != null) {
            page.setParent(pageRepository.findById(parentId).orElse(null));
        }
    }

    private void loadExisting(long id) {
        pageRepository.findById(id).ifPresentOrElse(
                foundPage -> {
                    page = foundPage;
                    parentId = Optional.ofNullable(foundPage.getParent()).map(Page::getId).orElse(null);
                },
                () -> FacesUtils.respondWithError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Page with id " + id + " not found"
                )
        );
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


    public void validateSlug(FacesContext context, UIComponent toValidate, String slug) {
        if (Pattern.compile("[^a-z0-9\\-]").matcher(slug).find()) {
            final var message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Only lowercase letters (a-z), numbers (0-9) and dashes (-) are allowed",
                    null
            );
            throw new ValidatorException(message);
        }
    }


    /// Create a new page with data set to this view model.
    public void create() {
        pageRepository.insert(page);
        id = page.getId();
        FacesContext.getCurrentInstance()
                .addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Page create", null));
        prettyFacesRedirector.redirect("pretty:viewPage", id);
    }

    /// Update the page with data set to this view model.
    public void update() {
        pageRepository.update(page);
        FacesContext.getCurrentInstance()
                .addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Page saved", null));
    }

    /// Delete this page and navigate to the page list.
    public void delete() {
        if (id != null) {
            pageService.deleteRecursively(page);
        }
        prettyFacesRedirector.redirect("pretty:adminPageList");
    }
}
