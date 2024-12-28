package dev.kuehni.jeecms.config.page;

import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.model.setting.SettingTag;
import dev.kuehni.jeecms.service.IdentityService;
import dev.kuehni.jeecms.service.SettingsService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

/// A singleton class that is created when the app server starts.\
///
/// @see #init()
@Singleton
@Startup
public class Initialization {

    @Inject
    private SettingsService settingsService;
    @Inject
    private PageRepository pageRepository;
    @Inject
    private IdentityService identityService;

    /// Called on app startup. This immediately returns if initialization has already been completed.\
    /// Otherwise, creates a root page and an admin account.
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
