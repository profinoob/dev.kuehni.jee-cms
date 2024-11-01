package dev.kuehni.jeecms.user.data;

import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository extends CrudRepository<User, Long> {
    public UserRepository() {
        super(User.class);
    }
}
