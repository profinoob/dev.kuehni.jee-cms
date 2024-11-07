package dev.kuehni.jeecms.model.page;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uc_page_slug_per_parent", columnNames = {"parent_id", "slug"}))
public class Page {
    @Id
    @GeneratedValue
    private long id;

    @Nonnull
    @Column(nullable = false)
    private String slug = "";

    @Nonnull
    @Column(nullable = false)
    private String title = "";

    @Nonnull
    @Column(length = 4096, nullable = false)
    private String content = "";

    @ManyToOne(fetch = FetchType.LAZY)
    private Page parent;


    public long getId() {
        return id;
    }

    @Nonnull
    public String getSlug() {
        return slug;
    }

    public void setSlug(@Nonnull String slug) {
        this.slug = Objects.requireNonNull(slug, "slug");
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nonnull String title) {
        this.title = Objects.requireNonNull(title, "title");
    }

    @Nonnull
    public String getContent() {
        return content;
    }

    public void setContent(@Nonnull String content) {
        this.content = Objects.requireNonNull(content, "content");
    }

    /** {@code null} means that {@code this} is the index page */
    @Nullable
    public Page getParent() {
        return parent;
    }

    public void setParent(@Nullable Page parent) {
        this.parent = parent;
    }
}
