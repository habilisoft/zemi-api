package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.Username;

import java.time.LocalDateTime;
import java.util.Objects;

public record DeleteUser(Username username,
                         String user,
                         LocalDateTime time) implements Command {
    public DeleteUser {
        Objects.requireNonNull(username, "Username is required");
    }
}
