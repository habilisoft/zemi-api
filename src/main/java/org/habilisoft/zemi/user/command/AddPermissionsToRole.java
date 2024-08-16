package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public record AddPermissionsToRole(RoleName roleName,
                                   Set<PermissionName> permissions,
                                   String user,
                                   LocalDateTime time) implements Command {
    public AddPermissionsToRole {
        Objects.requireNonNull(roleName, "Role name is required");
        Objects.requireNonNull(permissions, "Permissions are required");
    }
}
