package dev.kuehni.jeecms.model.setting;

import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SettingRepository extends CrudRepository<Setting, SettingTag> {

    public SettingRepository() {
        super(Setting.class);
    }
}
