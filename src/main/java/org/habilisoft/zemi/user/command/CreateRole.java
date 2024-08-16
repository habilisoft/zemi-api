package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public record CreateRole(RoleName name,
                         String description,
                         Set<PermissionName> permissions,
                         String user,
                         LocalDateTime time) implements Command {
    public CreateRole {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(description, "Description is required");
        Objects.requireNonNull(permissions, "Permissions are required");
    }
}
