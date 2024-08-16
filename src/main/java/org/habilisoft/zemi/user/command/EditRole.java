package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;

import java.time.LocalDateTime;
import java.util.Set;

public record EditRole(RoleName roleName,
                       String description,
                       Set<PermissionName> permissions,
                       String user,
                       LocalDateTime time) implements Command {
}
