package dev.kuehni.jeecms.auth;

import dev.kuehni.jeecms.auth.crypto.PasswordHasher;
import dev.kuehni.jeecms.auth.data.Identity;
import dev.kuehni.jeecms.auth.data.IdentityRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IdentityService {
    @Inject
    private IdentityRepository identityRepository;
    @Inject
    private PasswordHasher passwordHasher;

    public boolean isUsernameTaken(@Nonnull final String username) {
        return identityRepository.existsByUsername(username);
    }

    public void register(@Nonnull final String username, @Nonnull final String password) {
        final var identity = new Identity();
        identity.setUsername(username);
        identity.setPasswordHash(passwordHasher.hashPassword(password));
        identityRepository.insert(identity);
    }
}
