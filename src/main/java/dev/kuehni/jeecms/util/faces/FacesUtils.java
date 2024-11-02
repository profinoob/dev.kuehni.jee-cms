package dev.kuehni.jeecms.util.faces;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.faces.component.UIComponent;

import java.util.Objects;

public class FacesUtils {
    private FacesUtils() {}

    /**
     * Searches through a tree of {@link UIComponent}s for any component that has an {@link UIComponent#getId() id} equal to
     * {@code id} starting with the root {@code parent}.
     * <p>
     * If the {@code parent}'s {@link UIComponent#getId() id} matches {@code id}, the {@code parent} is returned.<br>
     * Otherwise, the {@code parent}'s {@link UIComponent#getChildren() children} are checked recursively.
     * <p>
     * If no {@link UIComponent} in the tree has an {@link UIComponent#getId() id} equal to {@code id}, {@code null}
     * is returned.
     */
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
}