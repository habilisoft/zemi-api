package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.domain.RoleName;

@Getter
public class RoleAlreadyExistsException extends DomainException {
    private final RoleName roleName;

    public RoleAlreadyExistsException(RoleName roleName) {
        super("Role %s already exists".formatted(roleName.value()));
        this.roleName = roleName;
    }
}
