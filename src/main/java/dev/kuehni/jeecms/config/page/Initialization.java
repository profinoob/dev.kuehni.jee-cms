package dev.kuehni.jeecms.config.page;

import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.model.setting.SettingTag;
import dev.kuehni.jeecms.service.IdentityService;
import dev.kuehni.jeecms.service.SettingsService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class Initialization {

    @Inject
    private SettingsService settingsService;
    @Inject
    private PageRepository pageRepository;
    @Inject
    private IdentityService identityService;

    @PostConstruct
    public synchronized void init() {
        final var initializationComplete = settingsService.getBooleanOrFalse(SettingTag.INITIALIZATION_COMPLETE);
        if (initializationComplete) {
            return;
        }

        pageRepository.createRootPage();
        identityService.createAdmin();

        settingsService.set(SettingTag.INITIALIZATION_COMPLETE, true);
    }
}
