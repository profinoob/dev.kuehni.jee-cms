package dev.kuehni.jeecms.service;

import dev.kuehni.jeecms.model.setting.Setting;
import dev.kuehni.jeecms.model.setting.SettingRepository;
import dev.kuehni.jeecms.model.setting.SettingTag;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class SettingsService {
    @Inject
    private SettingRepository settingRepository;

    public void set(@Nonnull SettingTag tag, @Nonnull String value) {
        settingRepository.findById(tag).ifPresentOrElse(setting -> {
            setting.setValue(value);
            settingRepository.update(setting);
        }, () -> settingRepository.insert(new Setting(tag, value)));
    }

    public void set(@Nonnull SettingTag tag, boolean value) {
        set(tag, String.valueOf(value));
    }

    @Nonnull
    public Optional<String> getOrEmpty(@Nonnull SettingTag tag) {
        return settingRepository.findById(tag).map(Setting::getValue);
    }

    public boolean getBooleanOrFalse(@Nonnull SettingTag tag) {
        return getOrEmpty(tag).map(Boolean::valueOf).orElse(false);
    }
}
