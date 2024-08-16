package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.Username;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record AddRolesToUser(Username username,
                             Set<RoleName> roles,
                             String user,
                             LocalDateTime time) implements Command {
    public AddRolesToUser {
        Objects.requireNonNull(username, "Username is required");
        Objects.requireNonNull(roles, "Roles are required");
    }
}
