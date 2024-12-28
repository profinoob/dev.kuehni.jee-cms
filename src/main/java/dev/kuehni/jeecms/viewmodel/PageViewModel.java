package dev.kuehni.jeecms.viewmodel;

import dev.kuehni.jeecms.model.comment.Comment;
import dev.kuehni.jeecms.model.comment.CommentRepository;
import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.model.page.PageRepository;
import dev.kuehni.jeecms.service.PageService;
import dev.kuehni.jeecms.util.faces.FacesUtils;
import dev.kuehni.jeecms.util.page.PagePath;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Named
@RequestScoped
public class PageViewModel {
    private PagePath path;

    private Page page;

    private List<Comment> comments;


    @Inject
    private PageRepository pageRepository;
    @Inject
    private CommentRepository commentRepository;
    @Inject
    private PageService pageService;

    /// Try to load the page with the set path or return a 404.
    public void load() {
        pageService.getAtPath(path).ifPresentOrElse(
                page -> this.page = page,
                () -> FacesUtils.respondWithError(HttpServletResponse.SC_NOT_FOUND, "Page not found: " + path)
        );

        if (page != null) {
            this.comments = commentRepository.findByPage(page);
        }
    }

    /// Load the root page.
    public void loadRoot() {
        page = pageService.getRoot();
        path = PagePath.ROOT;

        this.comments = commentRepository.findByPage(page);
    }

    public void setPathWithoutLeadingSlash(@Nonnull String path) {
        this.path = PagePath.parseWithoutLeadingSlash(path);
    }

    @Nonnull
    public String getPathWithoutLeadingSlash() {
        return this.path.toString().substring(1);
    }

    public void addComment(@Nonnull Comment comment) {
        comments.addFirst(comment);
    }

    @Nonnull
    public String getTitle() {
        return page.getTitle();
    }

    @Nonnull
    public String getContent() {
        return page.getContent();
    }


    public Page getPage() {
        return page;
    }

    @Nullable
    public Page getParent() {
        return page.getParent();
    }

    public boolean hasChildren() {
        return pageRepository.existsChildrenOf(page);
    }

    @Nonnull
    public List<Page> getChildren() {
        return pageRepository.findChildren(page);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getCommentCount() {
        return comments.size();
    }
}
