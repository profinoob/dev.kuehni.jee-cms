package dev.kuehni.jeecms.service.auth;

import dev.kuehni.jeecms.service.crypto.PasswordService;
import dev.kuehni.jeecms.model.identity.IdentityRepository;
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

    public AuthResult authenticate(@Nonnull String username, @Nonnull String password) {
        final var optionalIdentity = identityRepository.findByUsername(username);
        return optionalIdentity.map(identity -> {
            if (!passwordService.verifyPassword(password, identity.getPasswordHash())) {
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
