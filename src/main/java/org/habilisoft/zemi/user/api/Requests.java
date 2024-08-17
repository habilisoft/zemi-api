package org.habilisoft.zemi.user.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;

import java.util.Set;

public interface Requests {
    record RemoveRolesFromUser(@NotEmpty Set<RoleName> roles) { }
    record RemovePermissionsFromRole(Set<PermissionName> permissions) { }

    record CreateUser(
            @NotBlank(message = "Username is required")
            Username username,
            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password,
            @NotBlank(message = "Name is required")
            String name,
            Set<RoleName> roles,
            Boolean changePasswordAtNextLogin
    ) { }

    record CreateRole(
            @NotEmpty RoleName name,
            @NotEmpty String description,
            @NotEmpty Set<PermissionName> permissions
    ) { }

    record ChangePassword(
            @NotNull
            @Size(min = 8, message = "Password must be at least 8 characters")
            String currentPassword,
            @NotNull
            @Size(min = 8, message = "Password must be at least 8 characters")
            String newPassword
    ) { }

    record AssignRoleToUsers(@NotEmpty Set<Username> users) { }

    record AssignPermissionsToRole(Set<PermissionName> permissions) { }

    record AddRolesToUser(@NotEmpty Set<RoleName> roles) { }

    record ResetPassword(
            @NotNull Username username,
            @NotNull
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password,
            Boolean changePasswordAtNextLogin
    ) { }
}
