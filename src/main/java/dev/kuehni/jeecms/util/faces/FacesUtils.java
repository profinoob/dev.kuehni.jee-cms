package dev.kuehni.jeecms.util.faces;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public class FacesUtils {
    private FacesUtils() {}

    /// Searches through a tree of [UIComponent]s for any component that has an {@link UIComponent#getId() id} equal to
    /// `id` starting with the root `parent`.
    ///
    /// If the `parent`'s {@link UIComponent#getId() id} matches `id`, the `parent` is returned.\
    /// Otherwise, the `parent`'s {@link UIComponent#getChildren() children} are checked recursively.
    ///
    /// If no [UIComponent] in the tree has an {@link UIComponent#getId() id} equal to `id`, `null`
    /// is returned.
    @Nullable
    public static UIComponent findComponentById(@Nonnull final UIComponent parent, @Nonnull final String id) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(id, "id");

        if (id.equals(parent.getId())) {
            return parent;
        }

        return parent.getChildren()
                .stream()
                .map(child -> findComponentById(child, id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /// Mark the current request as complete with the given HTTP `status` and `message`.
    public static void respondWithError(int status, @Nullable final String message) {
        final var facesContext = FacesContext.getCurrentInstance();
        final var response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        try {
            if (message != null) {
                response.sendError(status, message);
            } else {
                response.sendError(status);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        facesContext.responseComplete(); // Stops further processing
    }

    /// Mark the current request as complete with the given HTTP `status`.
    public static void respondWithError(int status) {
        respondWithError(status, null);
    }
}
