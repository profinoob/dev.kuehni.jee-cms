package dev.kuehni.jeecms.model.identity;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Identity {

    @Id
    @GeneratedValue
    private Long id;

    @Nonnull
    @Column(nullable = false, unique = true)
    private String username = "";

    @Nonnull
    @Column(nullable = false)
    private String passwordHash = "";

    @Nonnull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdentityRole role = IdentityRole.USER;


    public long getId() {
        return id;
    }

    @Nonnull
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nonnull String username) {
        this.username = username;
    }

    @Nonnull
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(@Nonnull String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Nonnull
    public IdentityRole getRole() {
        return role;
    }

    public void setRole(@Nonnull IdentityRole role) {
        this.role = Objects.requireNonNull(role, "role");
    }

    public boolean is(@Nonnull IdentityRole role) {
        return this.role.equals(role);
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof Identity other))
            return false;

        if (id == other.getId()) return true;
        if (id == null) return false;

        // equivalence by id
        return id.equals(other.getId());
    }

    public int hashCode() {
        if (id != null) return id.hashCode();
        return super.hashCode();
    }
}
