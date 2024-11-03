package dev.kuehni.jeecms.page;

import dev.kuehni.jeecms.page.data.Page;
import dev.kuehni.jeecms.page.data.PageRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ApplicationScoped
public class PageService {
    @Inject
    private PageRepository pageRepository;

    @Nonnull
    public List<Page> getAll() {
        return pageRepository.findAll();
    }
}
