package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.RoleName;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public record CreateUser(Username username,
                         String name,
                         String password,
                         Boolean changePasswordAtNextLogin,
                         Set<RoleName> roles,
                         String user,
                         LocalDateTime time) implements Command {
    public CreateUser {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(username, "Email is required");
        Objects.requireNonNull(password, "Password is required");
    }
}
