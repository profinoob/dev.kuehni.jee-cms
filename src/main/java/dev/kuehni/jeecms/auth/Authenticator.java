package dev.kuehni.jeecms.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import java.util.Set;

@ApplicationScoped
public class Authenticator implements IdentityStore {
    @Override
    public CredentialValidationResult validate(Credential credential) {
        var userCredentials = (UsernamePasswordCredential) credential;
        var username = userCredentials.getCaller();
        var password = userCredentials.getPasswordAsString();

        if ("username".equals(username) && "password".equals(password)) {
            return new CredentialValidationResult(username, Set.of("user"));
        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}
