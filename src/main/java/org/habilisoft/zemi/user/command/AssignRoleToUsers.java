package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.Username;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public record AssignRoleToUsers(RoleName roleName,
                                Set<Username> usernames,
                                String user,
                                LocalDateTime time) implements Command {
    public AssignRoleToUsers {
        Objects.requireNonNull(roleName, "Role ID is required");
        Objects.requireNonNull(usernames, "Usernames are required");
    }
}
