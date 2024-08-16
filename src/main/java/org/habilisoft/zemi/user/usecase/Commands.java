package org.habilisoft.zemi.user.usecase;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public interface Commands {
    record AddPermissionsToRole(RoleName roleName,
                                Set<PermissionName> permissions,
                                String user,
                                LocalDateTime time) implements Command {
        public AddPermissionsToRole {
            Objects.requireNonNull(roleName, "Role name is required");
            Objects.requireNonNull(permissions, "Permissions are required");
        }
    }

    record AddRolesToUser(Username username,
                          Set<RoleName> roles,
                          String user,
                          LocalDateTime time) implements Command {
        public AddRolesToUser {
            Objects.requireNonNull(username, "Username is required");
            Objects.requireNonNull(roles, "Roles are required");
        }
    }

    record AssignRoleToUsers(RoleName roleName,
                             Set<Username> usernames,
                             String user,
                             LocalDateTime time) implements Command {
        public AssignRoleToUsers {
            Objects.requireNonNull(roleName, "Role ID is required");
            Objects.requireNonNull(usernames, "Usernames are required");
        }
    }

    record ChangePassword(Username username,
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

    record CreateRole(RoleName name,
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

    record CreateUser(Username username,
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

    record DeleteRole(RoleName roleName, String user, LocalDateTime time) implements Command { }

    record DeleteUser(Username username,
                      String user,
                      LocalDateTime time) implements Command {
        public DeleteUser {
            Objects.requireNonNull(username, "Username is required");
        }
    }

    record EditRole(RoleName roleName,
                    String description,
                    Set<PermissionName> permissions,
                    String user,
                    LocalDateTime time) implements Command {
    }

    record RemovePermissionsFromRole(RoleName roleName,
                                     Set<PermissionName> permissions,
                                     String user,
                                     LocalDateTime time) implements Command {
        public RemovePermissionsFromRole {
            Objects.requireNonNull(roleName, "Role ID is required");
            Objects.requireNonNull(permissions, "Permissions are required");
        }
    }

    record RemoveRolesFromUser(Username username,
                               Set<RoleName> roles,
                               String user,
                               LocalDateTime time) implements Command {
        public RemoveRolesFromUser {
            Objects.requireNonNull(username, "Username is required");
            Objects.requireNonNull(roles, "Roles are required");
        }
    }

    record ResetPassword(Username username,
                         String password,
                         Boolean changePasswordAtNextLogin,
                         String user,
                         LocalDateTime time) implements Command {
        public ResetPassword {
            Objects.requireNonNull(username, "Username is required");
            Objects.requireNonNull(password, "Password is required");
        }
    }
}
