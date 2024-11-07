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

@Named
@SessionScoped
public class AuthBean implements Serializable {

    @Inject
    private IdentityRepository identityRepository;

    @Nonnull
    private AuthState authState = new LoggedOut();

    @Nullable
    private String redirectToAfterLogin;

    @Nonnull
    public AuthState getAuthState() {
        return authState;
    }

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
        authState = new LoggedOut();
        this.redirectToAfterLogin = null;
    }

    public void setRedirectToAfterLogin(@Nullable final String redirectToAfterLogin) {
        this.redirectToAfterLogin = redirectToAfterLogin;
    }

    @Nullable
    public String getRedirectToAfterLogin() {
        return redirectToAfterLogin;
    }


    public interface AuthState {

        boolean isLoggedIn();

        @Nullable
        String getLoggedInUsername();
    }

    static class LoggedOut implements AuthState {

        @Override
        public boolean isLoggedIn() {
            return false;
        }

        @Nullable
        @Override
        public String getLoggedInUsername() {
            return "";
        }
    }

    static class LoggedIn implements AuthState {
        private final long identityId;

        @Nonnull
        private final String username;

        public LoggedIn(@Nonnull final Identity identity) {
            Objects.requireNonNull(identity, "identity");

            this.identityId = identity.id;
            this.username = identity.getUsername();
        }

        @Override
        public boolean isLoggedIn() {
            return true;
        }

        @Nonnull
        @Override
        public String getLoggedInUsername() {
            return username;
        }
    }
}
