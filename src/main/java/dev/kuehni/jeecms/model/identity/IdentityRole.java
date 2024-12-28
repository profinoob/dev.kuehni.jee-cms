package dev.kuehni.jeecms.model.identity;

import jakarta.annotation.Nonnull;

/// Represents a RBAC role for an account
public enum IdentityRole {
    /// An administrator user that has all permissions
    ADMINISTRATOR,
    /// An author which is allowed to create/edit pages
    AUTHOR,
    /// A user with no special permission other than adding comments to pages
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
