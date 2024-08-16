package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.domain.PermissionName;

@Getter
public final class PermissionNotFoundException extends DomainException {
    private final PermissionName permissionName;

    public PermissionNotFoundException(PermissionName permissionName) {
        super("Permission %s not found".formatted(permissionName.value()));
        this.permissionName = permissionName;
    }
}
