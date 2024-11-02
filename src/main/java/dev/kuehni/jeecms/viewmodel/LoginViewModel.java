package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class LoginViewModel {
    private static final Logger logger = Logger.getLogger(LoginViewModel.class.getName());

    @Nonnull
    private String username = "";
    @Nonnull
    private String password = "";

    @Inject
    private PrettyFacesRedirector prettyFacesRedirector;
    @Inject
    private SecurityContext securityContext;

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
        final var externalContext = facesContext.getExternalContext();
        final var session = ((HttpServletRequest) externalContext.getRequest()).getSession();
        if (session.getAttribute("authInProgress") == null) {
            session.setAttribute("authInProgress", true);
            try {
                final var result = continueAuthentication();
                switch (result != null ? result : AuthenticationStatus.NOT_DONE) {
                    case AuthenticationStatus.SEND_CONTINUE -> facesContext.responseComplete();
                    case AuthenticationStatus.SEND_FAILURE -> {
                        facesContext.addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Login failed", null));
                    }
                    case AuthenticationStatus.SUCCESS -> {
                    }
                    case AuthenticationStatus.NOT_DONE -> {
                    }
                }
            } finally {
                session.removeAttribute("authInProgress");
            }
        } else {
            logger.log(Level.WARNING, "Authentication already in progress; skipping recursive call.");
        }

//        try {
//            ((HttpServletRequest) externalContext.getRequest()).login(username, password);
//        } catch (ServletException ex) {
//            logger.log(Level.WARNING, ex, ex::getMessage);
//            facesContext.addMessage(
//                    null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null)
//            );
//        }
    }

    private AuthenticationStatus continueAuthentication() {
        final var externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams().credential(
                        new UsernamePasswordCredential(username, password))
        );
    }
}
