package dev.kuehni.jeecms.model.identity;

import jakarta.annotation.Nonnull;

public enum IdentityRole {
    ADMINISTRATOR,
    AUTHOR,
    USER;

    @Nonnull
    public String getDisplayName() {
        return switch (this) {
            case ADMINISTRATOR -> "Administrator";
            case AUTHOR -> "Author";
            case USER -> "User";
        };
    }
}
