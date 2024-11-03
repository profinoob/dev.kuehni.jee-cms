package dev.kuehni.jeecms.config.page;

import dev.kuehni.jeecms.model.page.PageRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class RootPageCreator {
    @Inject
    private PageRepository pageRepository;

    @PostConstruct
    public void init() {
        pageRepository.createRootPageIfNotExists();
    }
}
