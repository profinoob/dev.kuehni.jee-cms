package dev.kuehni.jeecms.service;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.model.page.PageRepository;
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
