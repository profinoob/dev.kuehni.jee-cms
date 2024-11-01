package dev.kuehni.jeecms.user;

import dev.kuehni.jeecms.user.data.User;
import dev.kuehni.jeecms.user.data.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository userRepository;

    @Nonnull
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
