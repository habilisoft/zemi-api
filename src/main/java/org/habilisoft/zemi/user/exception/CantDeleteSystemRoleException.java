package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;

@Getter
public class CantDeleteSystemRoleException extends DomainException {
    public CantDeleteSystemRoleException() {
        super("Can't delete system role");
    }
}
