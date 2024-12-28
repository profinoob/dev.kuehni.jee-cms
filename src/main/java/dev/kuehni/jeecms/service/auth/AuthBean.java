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

/// This bean actually represents the auth session of a client.
@Named
@SessionScoped
public class AuthBean implements Serializable {

    @Inject
    private IdentityRepository identityRepository;

    @Nonnull
    private AuthState authState = AuthState.LOGGED_OUT;

    @Nullable
    private String redirectToAfterLogin;

    /// Check, if the supposedly logged-in user still matches the stored username.
    /// If that is not the case, the user is {@link AuthBean#logOut() logged out}.
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

    /// Transitions the session to the {@link LoggedIn logged-in} state for the given `identity`.
    public void logIn(@Nonnull final Identity identity) {
        authState = new LoggedIn(identity);
        this.redirectToAfterLogin = null;
    }

    /// Transitions the session to the {@link AuthState#LOGGED_OUT logged-out} state.
    public void logOut() {
        authState = AuthState.LOGGED_OUT;
        this.redirectToAfterLogin = null;
    }

    /// Store the given url to the session as a redirect for after logging in.
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
        /// Represents a logged-out state.
        AuthState LOGGED_OUT = new AuthState() {};

        /// Whether this represents a logged-in state.
        default boolean isLoggedIn() {
            return false;
        }

        /// The logged-in identity's id or `null` if this session counts as logged-out.
        @Nullable
        default Long getLoggedInIdentityId() {
            return null;
        }

        /// The logged-in identity's username or `null` if this session counts as logged-out.
        @Nullable
        default String getLoggedInUsername() {
            return null;
        }
    }

    /// Represents a logged-in state.
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
