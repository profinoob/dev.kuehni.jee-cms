package dev.kuehni.jeecms.auth;

import dev.kuehni.jeecms.auth.crypto.PasswordHasher;
import dev.kuehni.jeecms.auth.data.IdentityRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Authenticator {
    @Inject
    private AuthBean authBean;
    @Inject
    private IdentityRepository identityRepository;
    @Inject
    private PasswordHasher passwordHasher;

    public AuthResult authenticate(@Nonnull String username, @Nonnull String password) {
        final var optionalIdentity = identityRepository.findByUsername(username);
        return optionalIdentity.map(identity -> {
            if (!passwordHasher.verifyPassword(password, identity.getPasswordHash())) {
                return AuthResult.FAILURE;
            }
            authBean.logIn(username);
            return AuthResult.SUCCESS;
        }).orElse(AuthResult.FAILURE);
    }

    public enum AuthResult {
        FAILURE,
        SUCCESS,
    }
}
