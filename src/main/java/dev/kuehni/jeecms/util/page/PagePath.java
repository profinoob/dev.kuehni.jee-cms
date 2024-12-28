package dev.kuehni.jeecms.util.page;

import dev.kuehni.jeecms.model.page.Page;
import jakarta.annotation.Nonnull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/// Represents the location of a [Page].
public class PagePath {

    /// A path with no segments. Represents the root page.
    public static final PagePath ROOT = new PagePath(List.of());


    @Nonnull
    private final List<String> segments;

    private PagePath(@Nonnull final List<String> segments) {
        this.segments = segments;
    }

    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    /// Split the given `path` around slashes (`/`) into segments while ignoring any leading and trailing slashes.
    ///
    /// @return A [PagePath] that contains all parsed segments.
    @Nonnull
    public static PagePath parseWithoutLeadingSlash(@Nonnull final String path) {
        final var pathWithoutAffixes = path.replaceFirst("^/+", "").replaceFirst("/+$", "");
        if (pathWithoutAffixes.isBlank()) {
            return ROOT;
        }
        return new PagePath(List.of(pathWithoutAffixes.split("/")));
    }

    /// Whether this path has no segments, meaning it represents the root page.
    public boolean isRoot() {
        return segments.isEmpty();
    }

    @Nonnull
    public String getLastSegment() {
        return segments.getLast();
    }

    /// Returns a copy of this path but with the last segment removed.
    @Nonnull
    public PagePath withoutLastSegment() {
        return new PagePath(segments.subList(0, segments.size() - 1));
    }


    @Nonnull
    @Override
    public String toString() {
        return "/" + String.join("/", segments);
    }


    public static class Builder {
        private final Deque<String> segments = new ArrayDeque<>();

        private Builder() {}

        /// Add `segment` to the front of this path.
        @Nonnull
        public Builder prepend(@Nonnull final String segment) {
            segments.push(segment);
            return this;
        }

        @Nonnull
        public PagePath build() {
            return new PagePath(List.copyOf(segments));
        }
    }
}
