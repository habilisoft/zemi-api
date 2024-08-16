package org.habilisoft.zemi.user.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface Requests {
    record RemoveRolesFromUser(@NotEmpty List<String> roles) { }
    record RemovePermissionsFromRole(List<String> permissions) { }

    record CreateUser(
            @NotBlank(message = "Username is required")
            String username,
            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password,
            @NotBlank(message = "Name is required")
            String name,
            List<String> roles,
            Boolean changePasswordAtNextLogin
    ) { }

    record CreateRole(
            @NotEmpty String name,
            @NotEmpty String description,
            @NotEmpty List<String> permissions
    ) { }

    record ChangePassword(
            @NotNull
            @Size(min = 8, message = "Password must be at least 8 characters")
            String currentPassword,
            @NotNull
            @Size(min = 8, message = "Password must be at least 8 characters")
            String newPassword
    ) { }

    record AssignRoleToUsers(@NotEmpty List<String> users) { }

    record AssignPermissionsToRole(List<String> permissions) { }

    record AddRolesToUser(@NotEmpty List<String> roles) { }

    record ResetPassword(
            @NotNull String username,
            @NotNull
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password,
            Boolean changePasswordAtNextLogin
    ) { }
}
