package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.Username;

import java.time.LocalDateTime;
import java.util.Objects;

public record ResetPassword(Username username,
                            String password,
                            Boolean changePasswordAtNextLogin,
                            String user,
                            LocalDateTime time) implements Command {
    public ResetPassword {
        Objects.requireNonNull(username, "Username is required");
        Objects.requireNonNull(password, "Password is required");
    }
}
