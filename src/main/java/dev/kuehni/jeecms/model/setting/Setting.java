package dev.kuehni.jeecms.model.setting;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table
public class Setting {

    @Id
    @Column(name = "tag", nullable = false)
    @Enumerated(EnumType.STRING)
    private SettingTag settingTag;

    @Nonnull
    @Column(nullable = false)
    private String value = "";


    public Setting() {}

    public Setting(@Nonnull SettingTag settingTag, @Nonnull String value) {
        this.settingTag = Objects.requireNonNull(settingTag, "settingTag");
        this.value = Objects.requireNonNull(value, "value");
    }


    @Nonnull
    public String getValue() {
        return value;
    }

    public void setValue(@Nonnull String value) {
        this.value = Objects.requireNonNull(value, "value");
    }
}
