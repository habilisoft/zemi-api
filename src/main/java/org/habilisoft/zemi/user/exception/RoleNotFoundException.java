package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.domain.RoleName;

@Getter
public class RoleNotFoundException extends DomainException {
    private final RoleName roleName;

    public RoleNotFoundException(RoleName roleName) {
        super("Role with name %s not found".formatted(roleName));
        this.roleName = roleName;
    }
}
