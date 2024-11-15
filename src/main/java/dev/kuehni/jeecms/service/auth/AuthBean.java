package dev.kuehni.jeecms.service.auth;

import dev.kuehni.jeecms.model.identity.Identity;
import dev.kuehni.jeecms.model.identity.IdentityRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Named
@SessionScoped
public class AuthBean implements Serializable {

    @Inject
    private IdentityRepository identityRepository;

    @Nonnull
    private AuthState authState = AuthState.LOGGED_OUT;

    @Nullable
    private String redirectToAfterLogin;

    public void refresh() {
        if (authState instanceof LoggedIn loggedIn) {
            final var loggedInUsername = loggedIn.getLoggedInUsername();
            final var isStillValid = identityRepository.findById(loggedIn.identityId)
                    .map(Identity::getUsername)
                    .filter(loggedInUsername::equals)
                    .isPresent();
            if (!isStillValid) {
                logOut();
            }
        }
    }

    public void logIn(@Nonnull final Identity identity) {
        authState = new LoggedIn(identity);
        this.redirectToAfterLogin = null;
    }

    public void logOut() {
        authState = AuthState.LOGGED_OUT;
        this.redirectToAfterLogin = null;
    }

    public void setRedirectToAfterLogin(@Nullable final String redirectToAfterLogin) {
        this.redirectToAfterLogin = redirectToAfterLogin;
    }

    @Nullable
    public String getRedirectToAfterLogin() {
        return redirectToAfterLogin;
    }


    public boolean isLoggedIn() {
        return authState.isLoggedIn();
    }

    @Nullable
    public Identity getLoggedInIdentity() {
        return Optional.ofNullable(authState.getLoggedInIdentityId())
                .flatMap(identityRepository::findById)
                .orElse(null);
    }

    @Nullable
    public String getLoggedInUsername() {
        return authState.getLoggedInUsername();
    }


    private interface AuthState {
        AuthState LOGGED_OUT = new AuthState() {};

        default boolean isLoggedIn() {
            return false;
        }

        @Nullable
        default Long getLoggedInIdentityId() {
            return null;
        }

        @Nullable
        default String getLoggedInUsername() {
            return null;
        }
    }

    private static class LoggedIn implements AuthState {
        private final long identityId;

        @Nonnull
        private final String username;

        public LoggedIn(@Nonnull final Identity identity) {
            Objects.requireNonNull(identity, "identity");

            this.identityId = identity.getId();
            this.username = identity.getUsername();
        }

        @Override
        public boolean isLoggedIn() {
            return true;
        }

        @Nonnull
        @Override
        public Long getLoggedInIdentityId() {
            return identityId;
        }

        @Nonnull
        @Override
        public String getLoggedInUsername() {
            return username;
        }
    }
}
