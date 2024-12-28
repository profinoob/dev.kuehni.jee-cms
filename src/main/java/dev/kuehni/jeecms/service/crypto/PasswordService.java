package dev.kuehni.jeecms.service.crypto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.HashMap;

@ApplicationScoped
public class PasswordService {
    @Inject
    private Pbkdf2PasswordHash hasher;

    /// Set up the password hasher.
    @PostConstruct
    public void init() {
        final var params = new HashMap<String, String>();
        params.put("Pbkdf2PasswordHash.Iterations", "2048");
        params.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256");
        params.put("Pbkdf2PasswordHash.SaltSizeBytes", "32");
        hasher.initialize(params);
    }

    /// Generate a hash for the given `password` (plain text).
    @Nonnull
    public String hashPassword(@Nonnull String password) {
        return hasher.generate(password.toCharArray());
    }

    /// Check whether the given plain text `password` matches the `hash`, meaning the same password was used to
    /// generate that hash.
    public boolean verifyPassword(@Nonnull String password, @Nonnull String hash) {
        return hasher.verify(password.toCharArray(), hash);
    }
}
