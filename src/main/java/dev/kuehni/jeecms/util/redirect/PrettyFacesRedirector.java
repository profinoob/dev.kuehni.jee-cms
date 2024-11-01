package dev.kuehni.jeecms.util.redirect;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.url.URLPatternParser;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;

import java.io.IOException;

@RequestScoped
public class PrettyFacesRedirector {
    /**
     * Redirect using a mapping id. This uses {@link FacesContext#getCurrentInstance()} to get a context for redirecting.
     *
     * @param mappingId The mapping id as defined by pretty-config.xml. E.g. {@code "pretty:viewUser"}
     * @param params    Additional path parameters. See {@link URLPatternParser#getMappedURL(Object...)}.
     */
    public void redirect(@Nonnull String mappingId, Object... params) {
        final var mapping = PrettyContext.getCurrentInstance().getConfig().getMappingById(mappingId);
        final var relativeUrl = mapping.getPatternParser().getMappedURL(params).toURL();

        final var facesContext = FacesContext.getCurrentInstance();
        final var url = facesContext.getExternalContext().getRequestContextPath() + relativeUrl;
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
