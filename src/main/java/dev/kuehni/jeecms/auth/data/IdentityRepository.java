package dev.kuehni.jeecms.auth.data;

import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

import java.util.Optional;

@ApplicationScoped
public class IdentityRepository extends CrudRepository<Identity, Long> {

    public IdentityRepository() {
        super(Identity.class);
    }

    @Nonnull
    public Optional<Identity> findByUsername(final String username) {
        try {
            final var identity = getEntityManager()
                    .createQuery("select u from Identity u where u.username = :username", Identity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(identity);
        } catch (final NoResultException ex) {
            return Optional.empty();
        }
    }
}
