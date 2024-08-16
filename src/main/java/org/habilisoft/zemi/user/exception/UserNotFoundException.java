package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.habilisoft.zemi.user.Username;

@Getter
public class UserNotFoundException extends DomainException {
    private final Username username;

    public UserNotFoundException(Username username) {
        super("User %s not found".formatted(username.value()));
        this.username = username;
    }
}
