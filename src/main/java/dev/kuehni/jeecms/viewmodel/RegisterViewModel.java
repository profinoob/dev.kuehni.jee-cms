package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.auth.Authenticator;
import dev.kuehni.jeecms.auth.IdentityService;
import dev.kuehni.jeecms.faces.FacesUtils;
import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.NoSuchElementException;

@Named
@RequestScoped
public class RegisterViewModel {
    @Nonnull
    private String username = "";
    @Nonnull
    private String newPassword = "";
    @Nonnull
    private String repeatPassword = "";

    @Inject
    private IdentityService identityService;
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
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@Nonnull String newPassword) {
        this.newPassword = newPassword;
    }

    @Nonnull
    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(@Nonnull String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void register() {
        final String password = getNewPassword();
        identityService.register(getUsername(), password);
        authenticator.authenticate(username, password);
        prettyFacesRedirector.redirect("pretty:home");
    }

    public void validateUsername(FacesContext context, UIComponent toValidate, String username) {
        if (identityService.isUsernameTaken(username)) {
            final var message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username is already taken.", null);
            throw new ValidatorException(message);
        }
    }

    public void validateRepeatPassword(FacesContext context, UIComponent toValidate, String repeatPassword) {
        final var compareToId = (String) toValidate.getAttributes().get("compareTo");
        final var compareToInput = (UIInput) FacesUtils.findComponentById(context.getViewRoot(), compareToId);
        if (compareToInput == null) {
            throw new NoSuchElementException("Cannot find field with id \"" + compareToId + "\".");
        }

        if (!repeatPassword.equals(compareToInput.getValue())) {
            final var message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match.", null);
            throw new ValidatorException(message);
        }
    }
}
