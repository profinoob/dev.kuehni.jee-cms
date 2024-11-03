package dev.kuehni.jeecms.model.identity;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_identity_username", columnNames = "username")
})
public class Identity {

    @Id
    @GeneratedValue
    public long id;

    public String username;

    public String passwordHash;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
