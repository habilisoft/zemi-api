package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.domain.RoleName;

@Getter
public final class RoleInUseException extends DomainException {
    private final RoleName roleName;

    public RoleInUseException(RoleName roleName, int userCount) {
        super("Role %s is in use by %s users".formatted(roleName, userCount));
        this.roleName = roleName;
    }
}
