package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.service.PageService;
import dev.kuehni.jeecms.util.page.PagePath;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Named
@RequestScoped
public class PageViewModel {
    private PagePath path;

    private Page page;


    @Inject
    private PageRepository pageRepository;
    @Inject
    private PageService pageService;

    public void load() {
        pageService.getAtPath(path).ifPresentOrElse(page -> this.page = page, () -> {
            final var facesContext = FacesContext.getCurrentInstance();
            final var response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found: " + path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            facesContext.responseComplete(); // Stops further processing
        });
    }

    public void loadRoot() {
        page = pageService.getRoot();
        path = PagePath.ROOT;
    }

    public void setPathWithoutLeadingSlash(@Nonnull String path) {
        this.path = PagePath.parseWithoutLeadingSlash(path);
    }

    @Nonnull
    public String getTitle() {
        return page.getTitle();
    }

    @Nonnull
    public String getContent() {
        return page.getContent();
    }


    @Nullable
    public Page getParent() {
        return page.getParent();
    }

    public boolean hasChildren() {
        return pageRepository.existsChildrenOf(page);
    }

    @Nonnull
    public List<Page> getChildren() {
        return pageRepository.findChildren(page);
    }
}
