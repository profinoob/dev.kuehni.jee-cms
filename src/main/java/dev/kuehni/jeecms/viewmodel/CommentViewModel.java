package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.model.comment.Comment;
import dev.kuehni.jeecms.model.comment.CommentRepository;
import dev.kuehni.jeecms.model.identity.Identity;
import dev.kuehni.jeecms.service.auth.AuthBean;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@Named
@RequestScoped
public class CommentViewModel {
    private final Clock clock;

    @Named
    @Inject
    private PageViewModel pageViewModel;
    @Named
    @Inject
    private AuthBean authBean;
    @Inject
    private CommentRepository commentRepository;


    @Nonnull
    private String content = "";


    @Inject
    public CommentViewModel() {
        this(Clock.systemDefaultZone());
    }

    public CommentViewModel(@Nonnull Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /// Post the entered comment to the page.
    public void submit() {
        final Identity loggedInIdentity = authBean.getLoggedInIdentity();
        Objects.requireNonNull(loggedInIdentity, "loggedInIdentity");

        final var createdAt = LocalDateTime.now(clock);

        final var comment = new Comment(pageViewModel.getPage(), loggedInIdentity, content, createdAt);
        commentRepository.insert(comment);
        pageViewModel.addComment(comment);
        content = "";
    }

    @Nonnull
    public String getContent() {
        return content;
    }

    public void setContent(@Nonnull String content) {
        this.content = Objects.requireNonNull(content, "content");
    }
}
