package dev.kuehni.jeecms.service.auth;

import dev.kuehni.jeecms.model.identity.Identity;
import dev.kuehni.jeecms.model.identity.IdentityRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class PermissionService {

    @Inject
    private AuthBean authBean;

    /// Whether the currently logged-in [Identity] is allowed to edit pages.\
    /// Also returns `false`, if the current session doesn't count as logged-in.
    public boolean isAllowedToEditPage() {
        final var identity = authBean.getLoggedInIdentity();
        if (identity == null) {
            return false;
        }
        return switch (identity.getRole()) {
            case ADMINISTRATOR, AUTHOR -> true;
            default -> false;
        };
    }

    /// Whether the currently logged-in [Identity] is allowed to manage users.\
    /// Also returns `false`, if the current session doesn't count as logged-in.
    public boolean isAllowedToManageUsers() {
        final var identity = authBean.getLoggedInIdentity();
        return identity != null && identity.is(IdentityRole.ADMINISTRATOR);
    }
}
