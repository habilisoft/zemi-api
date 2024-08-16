package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.Username;

@Getter
public class UserAlreadyExistsException extends DomainException {
    private final Username username;

    public UserAlreadyExistsException(Username username) {
        super("User %s already exists".formatted(username.value()));
        this.username = username;
    }
}
