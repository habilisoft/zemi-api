package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.Username;

import java.time.LocalDateTime;
import java.util.Objects;

public record ChangePassword(Username username,
                             String oldPassword,
                             String newPassword,
                             String user,
                             LocalDateTime time) implements Command {
    public ChangePassword {
        Objects.requireNonNull(username, "Username is required");
        Objects.requireNonNull(oldPassword, "Old password is required");
        Objects.requireNonNull(newPassword, "New password is required");
    }
}
