package dev.kuehni.jeecms.viewmodel.admin;

import dev.kuehni.jeecms.model.identity.Identity;
import dev.kuehni.jeecms.model.identity.IdentityRepository;
import dev.kuehni.jeecms.model.identity.IdentityRole;
import dev.kuehni.jeecms.service.auth.AuthBean;
import dev.kuehni.jeecms.service.auth.PermissionService;
import dev.kuehni.jeecms.util.faces.FacesUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import org.primefaces.event.CellEditEvent;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UserListViewModel implements Serializable {

    @Inject
    private IdentityRepository identityRepository;
    @Named
    @Inject
    private PermissionService permissionService;
    @Named
    @Inject
    private AuthBean authBean;


    private List<EditableIdentity> identities;


    @PostConstruct
    public void init() {
        if (!permissionService.isAllowedToManageUsers()) {
            FacesUtils.respondWithError(HttpServletResponse.SC_FORBIDDEN);
        }
        final var loggedInIdentity = authBean.getLoggedInIdentity();
        identities = identityRepository.findAll()
                .stream()
                .map(identity -> new EditableIdentity(identity, identity.equals(loggedInIdentity)))
                .toList();
    }

    public void saveAll() {
        if (!permissionService.isAllowedToManageUsers()) {
            FacesUtils.respondWithError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        identities.stream()
                .filter(EditableIdentity::isDirty)
                .forEach(editableIdentity -> {
                    identityRepository.update(editableIdentity.identity);
                    editableIdentity.dirty = false;
                });

        final var msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved!", null);
        FacesContext.getCurrentInstance().addMessage("growl", msg);
    }

    @Nonnull
    public List<EditableIdentity> getIdentities() {
        return identities;
    }

    @Nonnull
    public List<IdentityRole> getRoleList() {
        return List.of(IdentityRole.values());
    }

    public static class EditableIdentity {
        @Nonnull
        private final Identity identity;

        private final boolean self;

        private boolean dirty;

        private EditableIdentity(@Nonnull Identity identity, boolean self) {
            this.identity = identity;
            this.self = self;
        }

        @Nonnull
        public String getUsername() {
            return identity.getUsername();
        }

        @Nonnull
        public IdentityRole getRole() {
            return identity.getRole();
        }

        public void setRole(@Nonnull IdentityRole role) {
            if (self) return;
            identity.setRole(role);
            dirty = true;
        }

        public boolean isSelf() {
            return self;
        }

        private boolean isDirty() {
            return dirty;
        }
    }
}
