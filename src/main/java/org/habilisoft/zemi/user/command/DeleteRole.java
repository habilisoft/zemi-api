package org.habilisoft.zemi.user.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.domain.RoleName;

import java.time.LocalDateTime;

public record DeleteRole(RoleName roleName, String user, LocalDateTime time) implements Command { }
