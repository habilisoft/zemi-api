package org.habilisoft.zemi.user.exception;

import org.habilisoft.zemi.shared.DomainException;

public class UserCannotDeleteHimselfException extends DomainException {
    public UserCannotDeleteHimselfException() {
        super("User cannot delete himself");
    }
}
