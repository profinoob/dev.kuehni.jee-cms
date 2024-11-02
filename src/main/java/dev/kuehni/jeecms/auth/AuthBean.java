package dev.kuehni.jeecms.auth;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Objects;

@Named
@SessionScoped
public class AuthBean implements Serializable {

    @Nonnull
    private AuthState authState = new LoggedOut();

    @Nonnull
    public AuthState getAuthState() {
        return authState;
    }

    public void logIn(@Nonnull final String username) {
        authState = new LoggedIn(username);
    }

    public void logOut() {
        authState = new LoggedOut();
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

        @Nonnull
        private final String username;

        public LoggedIn(@Nonnull final String username) {
            this.username = Objects.requireNonNull(username, "username");
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
