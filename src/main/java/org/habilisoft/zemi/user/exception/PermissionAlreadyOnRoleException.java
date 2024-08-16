package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;

@Getter
public class PermissionAlreadyOnRoleException extends DomainException {
    private final RoleName roleName;
    private final PermissionName permission;

    public PermissionAlreadyOnRoleException(RoleName roleName, PermissionName permission) {
        super("Permission %s is already on role %s".formatted(permission.value(), roleName.value()));
        this.roleName = roleName;
        this.permission = permission;
    }
}
