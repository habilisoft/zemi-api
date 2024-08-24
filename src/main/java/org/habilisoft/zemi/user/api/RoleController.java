package org.habilisoft.zemi.user.api;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.user.UserService;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.usecase.UserCommands;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/roles")
public class RoleController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({"admin", "auth:role:create"})
    public void createRole(@Valid @RequestBody Requests.CreateRole request) {
        UserCommands.CreateRole createRole = new UserCommands.CreateRole(
                request.name(),
                request.description(),
                request.permissions(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.createRole(createRole);
    }

    @PostMapping("/{roleName}")
    @RolesAllowed({"admin", "auth:role:update"})
    public void updateRole(@PathVariable RoleName roleName,
                                           @Valid @RequestBody Requests.CreateRole request) {
        UserCommands.EditRole command = new UserCommands.EditRole(
                roleName,
                request.description(),
                request.permissions(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.editRole(command);
    }

    @PostMapping("{roleName}/permissions")
    @RolesAllowed({"admin", "auth:role:create"})
    public void assignPermissionsToRole(@PathVariable RoleName roleName,
                                                  @Valid @RequestBody Requests.AssignPermissionsToRole request) {
        UserCommands.AddPermissionsToRole command = new UserCommands.AddPermissionsToRole(
                roleName,
                request.permissions(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.addPermissionsToRole(command);
    }

    @DeleteMapping("/{roleName}/permissions")
    @RolesAllowed({"admin", "auth:role:delete"})
    public void removePermissionsFromRole(@PathVariable RoleName roleName,
                                                       @Valid @RequestBody Requests.RemovePermissionsFromRole request) {
        UserCommands.RemovePermissionsFromRole removePermissionsFromRole = new UserCommands.RemovePermissionsFromRole(
                roleName,
                request.permissions(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.removePermissionsFromRole(removePermissionsFromRole);
    }

    @PostMapping("{roleName}/assign")
    @RolesAllowed({"admin", "auth:user:update"})
    public void assignRoleToUsers(@PathVariable RoleName roleName,
                                               @Valid @RequestBody Requests.AssignRoleToUsers request) {
        UserCommands.AssignRoleToUsers assignRoleToUsers = new UserCommands.AssignRoleToUsers(
                roleName,
                request.users(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.assignRoleToUsers(assignRoleToUsers);
    }

    @DeleteMapping("/{roleName}")
    @RolesAllowed({"admin", "auth:role:delete"})
    public void deleteRole(@PathVariable RoleName roleName) {
        UserCommands.DeleteRole deleteRole = new UserCommands.DeleteRole(
                roleName,
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.deleteRole(deleteRole);
    }
}
