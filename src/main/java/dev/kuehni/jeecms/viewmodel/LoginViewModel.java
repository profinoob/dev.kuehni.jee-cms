package dev.kuehni.jeecms.login.viewmodel;

import dev.kuehni.jeecms.auth.AuthBean;
import dev.kuehni.jeecms.auth.Authenticator;
import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;

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
    @Named
    @Inject
    private AuthBean authBean;

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

        final var redirect = authBean.getRedirectToAfterLogin();
        final var authResult = authenticator.authenticate(username, password);
        switch (authResult) {
            case FAILURE -> facesContext.addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null)
            );
            case SUCCESS -> {
                if (redirect != null) {
                    try {
                        facesContext.getExternalContext().redirect(redirect);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    prettyFacesRedirector.redirect("pretty:home");
                }
            }
        }
    }
}
