package dev.kuehni.jeecms.model.comment;

import dev.kuehni.jeecms.model.identity.Identity;
import dev.kuehni.jeecms.model.page.Page;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/// A comment on a [Page].
@Entity
@Table
public class Comment {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Page page;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Identity creator;

    @Column(length = 1024, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Comment() {}

    /// Create a new comment
    ///
    /// @param page      The page the new comment is attached to
    /// @param creator   The user that wrote this comment
    /// @param content   The content of the comment
    /// @param createdAt When the comment was created
    public Comment(
            @Nonnull Page page,
            @Nonnull Identity creator,
            @Nonnull String content,
            @Nonnull LocalDateTime createdAt
    ) {
        this.page = Objects.requireNonNull(page, "page");
        this.creator = Objects.requireNonNull(creator, "creator");
        this.content = Objects.requireNonNull(content, "content");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }


    @Nonnull
    public Identity getCreator() {
        return Objects.requireNonNull(creator, "creator");
    }

    @Nonnull
    public String getContent() {
        return Objects.requireNonNull(content, "content");
    }

    @Nonnull
    public LocalDateTime getCreatedAt() {
        return Objects.requireNonNull(createdAt, "createdAt");
    }
}
