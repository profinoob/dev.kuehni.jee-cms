package dev.kuehni.jeecms.page.data;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Page {
    @Id
    @GeneratedValue
    private long id;

    @Nonnull
    private String slug;

    @Nonnull
    private String title;

    @Nonnull
    @Column(length = 4096)
    private String content;

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
}
