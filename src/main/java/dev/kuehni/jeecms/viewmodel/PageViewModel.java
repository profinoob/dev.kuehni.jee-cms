package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.service.PageService;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Named
@RequestScoped
public class PageViewModel {
    private Long id;

    private Page page;


    @Inject
    private PageRepository pageRepository;
    @Inject
    private PageService pageService;

    public void load() {
        pageRepository.findById(id).ifPresentOrElse(page -> this.page = page, () -> {
            final var facesContext = FacesContext.getCurrentInstance();
            final var response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page with id " + id + " not found");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            facesContext.responseComplete(); // Stops further processing
        });
    }

    public void loadRoot() {
        page = pageService.getRoot();
        id = page.getId();
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nonnull
    public String getTitle() {
        return page.getTitle();
    }

    @Nonnull
    public String getContent() {
        return page.getContent();
    }
}
