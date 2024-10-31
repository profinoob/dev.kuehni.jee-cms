package dev.kuehni.jeecms.viewmodel;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.Objects;

@Named
@RequestScoped
public class UserViewModel {

    private String username = "";
    private boolean submitted;

    @Nonnull
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nonnull String username) {
        this.username = Objects.requireNonNull(username, "username");
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    @Nullable
    public String submit() {
        submitted = true;
        return null;
    }
}
