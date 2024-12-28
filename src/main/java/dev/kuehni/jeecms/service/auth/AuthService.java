package dev.kuehni.jeecms.service.auth;

import dev.kuehni.jeecms.model.identity.IdentityRepository;
import dev.kuehni.jeecms.service.crypto.PasswordService;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {
    @Inject
    private AuthBean authBean;
    @Inject
    private IdentityRepository identityRepository;
    @Inject
    private PasswordService passwordService;

    /// Try to log in the account with the given `username` and `password`.
    public AuthResult authenticate(@Nonnull String username, @Nonnull String password) {
        final var optionalIdentity = identityRepository.findByUsername(username);
        return optionalIdentity.map(identity -> {
            if (!passwordService.verifyPassword(password, identity.getPasswordHash())) {
                return AuthResult.FAILURE;
            }
            authBean.logIn(identity);
            return AuthResult.SUCCESS;
        }).orElse(AuthResult.FAILURE);
    }

    /// The result of an authentication attempt.
    public enum AuthResult {
        /// Authentication failed (wrong username / password).
        FAILURE,
        /// Authentication was successful. The session now counts as logged-in.
        SUCCESS,
    }
}
