package dev.kuehni.jeecms.service;

import dev.kuehni.jeecms.model.identity.Identity;
import dev.kuehni.jeecms.model.identity.IdentityRepository;
import dev.kuehni.jeecms.model.identity.IdentityRole;
import dev.kuehni.jeecms.service.crypto.PasswordService;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IdentityService {
    @Inject
    private IdentityRepository identityRepository;
    @Inject
    private PasswordService passwordService;

    /// Whether there is a user with the given name.
    public boolean isUsernameTaken(@Nonnull final String username) {
        return identityRepository.existsByUsername(username);
    }

    /// Create and persist a new user with the given `username` and `password`.
    @Nonnull
    public Identity register(@Nonnull final String username, @Nonnull final String password) {
        final var identity = new Identity();
        identity.setUsername(username);
        identity.setPasswordHash(passwordService.hashPassword(password));
        identityRepository.insert(identity);
        return identity;
    }

    /// Create the admin account if it doesn't exist.
    ///
    /// @throws IllegalStateException if an account with the username "admin" exists already.
    public void createAdmin() {
        if (isUsernameTaken("admin")) {
            throw new IllegalStateException("admin already exists");
        }
        final var admin = register("admin", "admin");
        admin.setRole(IdentityRole.ADMINISTRATOR);
        identityRepository.update(admin);
    }
}
