package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.RoleName;

@Getter
public class RoleAlreadyOnUserException extends DomainException {
    private final Username username;
    private final RoleName roleName;

    public RoleAlreadyOnUserException(Username username, RoleName roleName) {
        super("Role %s is already on user %s".formatted(roleName.value(), username.value()));
        this.username = username;
        this.roleName = roleName;
    }
}
