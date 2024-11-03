package dev.kuehni.jeecms.model.identity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Identity {

    @Id
    @GeneratedValue
    public long id;

    @Nonnull
    @Column(nullable = false, unique = true)
    public String username = "";

    @Nonnull
    @Column(nullable = false)
    public String passwordHash = "";


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
}
