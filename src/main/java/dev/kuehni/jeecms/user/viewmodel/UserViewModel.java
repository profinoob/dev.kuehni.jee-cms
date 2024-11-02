package dev.kuehni.jeecms.user.viewmodel;

import dev.kuehni.jeecms.user.data.User;
import dev.kuehni.jeecms.user.data.UserRepository;
import dev.kuehni.jeecms.util.redirect.PrettyFacesRedirector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;

@Named
@RequestScoped
public class UserViewModel {
    @Nullable
    private Long id;

    @Nonnull
    private User user = new User();

    @Inject
    private UserRepository userRepository;
    @Inject
    private PrettyFacesRedirector prettyFacesRedirector;


    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @Nullable
    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(@Nonnull String username) {
        user.setUsername(username);
    }


    /// Load a user from {@link UserRepository} by the id set by {@link #setId(Long)}.
    @Nullable
    public String load() {
        var foundUser = Optional.ofNullable(id).flatMap(userRepository::findById);
        user = foundUser.orElseGet(User::new);
        return foundUser.isPresent() ? null : "User not found";
    }

    /// Create a new user with data set to this view model and redirect to the view page for the newly created user.
    public void create() {
        userRepository.insert(user);
        id = user.getId();

        prettyFacesRedirector.redirect("pretty:viewUser", id);
    }

    /// Delete this user and navigate to the user list.
    public void delete() {
        if (id != null) {
            userRepository.delete(id);
        }
        prettyFacesRedirector.redirect("pretty:home");
    }
}
