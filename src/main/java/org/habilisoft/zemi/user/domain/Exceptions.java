package org.habilisoft.zemi.user.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;

public interface Exceptions {
    final class InvalidOldPassword extends DomainException {
        public InvalidOldPassword() {
            super("Invalid old password");
        }
    }

    @Getter
    class PermissionAlreadyOnRole extends DomainException {
        private final RoleName roleName;
        private final PermissionName permission;

        public PermissionAlreadyOnRole(RoleName roleName, PermissionName permission) {
            super("Permission %s is already on role %s".formatted(permission.value(), roleName.value()));
            this.roleName = roleName;
            this.permission = permission;
        }
    }

    @Getter
    final class PermissionNotFound extends DomainException {
        private final PermissionName permissionName;

        public PermissionNotFound(PermissionName permissionName) {
            super("Permission %s not found".formatted(permissionName.value()));
            this.permissionName = permissionName;
        }
    }

    @Getter
    class RoleAlreadyExists extends DomainException {
        private final RoleName roleName;

        public RoleAlreadyExists(RoleName roleName) {
            super("Role %s already exists".formatted(roleName.value()));
            this.roleName = roleName;
        }
    }

    @Getter
    class RoleAlreadyOnUser extends DomainException {
        private final Username username;
        private final RoleName roleName;

        public RoleAlreadyOnUser(Username username, RoleName roleName) {
            super("Role %s is already on user %s".formatted(roleName.value(), username.value()));
            this.username = username;
            this.roleName = roleName;
        }
    }

    @Getter
    final class RoleInUse extends DomainException {
        private final RoleName roleName;

        public RoleInUse(RoleName roleName, int userCount) {
            super("Role %s is in use by %s users".formatted(roleName, userCount));
            this.roleName = roleName;
        }
    }

    @Getter
    class RoleNotFound extends DomainException {
        private final RoleName roleName;

        public RoleNotFound(RoleName roleName) {
            super("Role with name %s not found".formatted(roleName));
            this.roleName = roleName;
        }
    }

    @Getter
    class UserAlreadyExists extends DomainException {
        private final Username username;

        public UserAlreadyExists(Username username) {
            super("User %s already exists".formatted(username.value()));
            this.username = username;
        }
    }

    class UserCannotDeleteHimself extends DomainException {
        public UserCannotDeleteHimself() {
            super("User cannot delete himself");
        }
    }

    @Getter
    class UserNotFound extends DomainException {
        private final Username username;

        public UserNotFound(Username username) {
            super("User %s not found".formatted(username.value()));
            this.username = username;
        }
    }

    @Getter
    class CantDeleteSystemRole extends DomainException {
        public CantDeleteSystemRole() {
            super("Can't delete system role");
        }
    }

    @Getter
    final class AtLeastOnePermissionRequired extends DomainException {
        public AtLeastOnePermissionRequired() {
            super("At least one permission is required");
        }
    }
}
