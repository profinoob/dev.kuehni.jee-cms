package dev.kuehni.jeecms.model.identity;

import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class IdentityRepository extends CrudRepository<Identity, Long> {

    public IdentityRepository() {
        super(Identity.class);
    }

    @Nonnull
    private TypedQuery<Identity> queryByUsername(@Nonnull final String username) {
        return getEntityManager()
                .createQuery("select u from Identity u where u.username = :username", Identity.class)
                .setParameter("username", Objects.requireNonNull(username, "username"));
    }

    /// Returns the account with the given `username`, if one exists. Otherwise, returns an empty optional.
    @Nonnull
    public Optional<Identity> findByUsername(@Nonnull final String username) {
        try {
            final var identity = queryByUsername(username).getSingleResult();
            return Optional.of(identity);
        } catch (final NoResultException ex) {
            return Optional.empty();
        }
    }

    /// Returns `true` if there is an account with the given `username`. Returns `false` otherwise
    public boolean existsByUsername(@Nonnull final String username) {
        return !queryByUsername(username)
                .setMaxResults(1)
                .getResultList().isEmpty();
    }
}
