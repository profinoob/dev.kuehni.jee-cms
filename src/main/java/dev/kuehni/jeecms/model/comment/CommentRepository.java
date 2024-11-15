package dev.kuehni.jeecms.model.comment;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class CommentRepository extends CrudRepository<Comment, Long> {
    public CommentRepository() {
        super(Comment.class);
    }

    @Nonnull
    public List<Comment> findByPage(@Nonnull Page page) {
        Objects.requireNonNull(page, "page");

        return getEntityManager().createQuery(
                "select c from Comment as c where c.page = :page order by c.createdAt desc",
                Comment.class
        ).setParameter("page", page).getResultList();
    }
}
