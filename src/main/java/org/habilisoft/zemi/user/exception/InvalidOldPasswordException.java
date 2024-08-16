package org.habilisoft.zemi.user.exception;

import org.habilisoft.zemi.shared.DomainException;

public final class InvalidOldPasswordException extends DomainException {
    public InvalidOldPasswordException() {
        super("Invalid old password");
    }
}
