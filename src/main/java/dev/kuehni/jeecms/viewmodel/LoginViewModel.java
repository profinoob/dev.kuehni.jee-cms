package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.auth.Authenticator;
import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class LoginViewModel {
    @Nonnull
    private String username = "";
    @Nonnull
    private String password = "";

    @Inject
    private PrettyFacesRedirector prettyFacesRedirector;
    @Inject
    private Authenticator authenticator;

    @Nonnull
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nonnull String username) {
        this.username = username;
    }

    @Nonnull
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nonnull String password) {
        this.password = password;
    }


    public void login() {
        final var facesContext = FacesContext.getCurrentInstance();

        final var authResult = authenticator.authenticate(username, password);
        switch (authResult) {
            case FAILURE -> facesContext.addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null)
            );
            case SUCCESS -> prettyFacesRedirector.redirect("pretty:home");
        }
    }
}
