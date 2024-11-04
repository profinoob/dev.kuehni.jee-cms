package dev.kuehni.jeecms.viewmodel.admin;

import dev.kuehni.jeecms.model.page.Page;
import dev.kuehni.jeecms.service.PageService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import java.io.Serializable;
import java.util.Objects;

@Named
@ViewScoped
public class PageTreeViewModel implements Serializable {
    private TreeNode<Page> tree;

    @Nullable
    private TreeNode<Page> selectedNode;

    @Inject
    private PageService pageService;

    @PostConstruct
    public void init() {
        tree = new DefaultTreeNode<>(new Page());
        var rootPageNode = new DefaultTreeNode<>(pageService.getRoot(), tree);
        addChildrenRecursive(rootPageNode);
    }

    private void addChildrenRecursive(@Nonnull TreeNode<Page> parentNode) {
        final var children = pageService.getChildrenOf(parentNode.getData());
        children.forEach(child -> addChildrenRecursive(new DefaultTreeNode<>(child, parentNode)));
    }

    @Nonnull
    public TreeNode<Page> getTree() {
        return Objects.requireNonNull(tree, "root");
    }

    @Nullable
    public TreeNode<Page> getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(@Nullable TreeNode<Page> selectedNode) {
        this.selectedNode = selectedNode;
    }
}
