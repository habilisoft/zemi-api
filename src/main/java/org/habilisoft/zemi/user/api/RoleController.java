package org.habilisoft.zemi.user.api;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.user.UserService;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.usecase.Commands;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/roles")
public class RoleController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({"admin", "auth:role:create"})
    public void createRole(@Valid @RequestBody Requests.CreateRole request) {
        Commands.CreateRole createRole = new Commands.CreateRole(
                RoleName.from(request.name()),
                request.description(),
                request.permissions().stream().map(PermissionName::from).collect(Collectors.toSet()),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.createRole(createRole);
    }

    @PostMapping("/{roleName}")
    @RolesAllowed({"admin", "auth:role:update"})
    public void updateRole(@PathVariable String roleName,
                                           @Valid @RequestBody Requests.CreateRole request) {
        Commands.EditRole command = new Commands.EditRole(
                RoleName.from(roleName),
                request.description(),
                request.permissions().stream().map(PermissionName::from).collect(Collectors.toSet()),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.editRole(command);
    }

    @PostMapping("{roleName}/permissions")
    @RolesAllowed({"admin", "auth:role:create"})
    public void assignPermissionsToRole(@PathVariable String roleName,
                                                  @Valid @RequestBody Requests.AssignPermissionsToRole request) {
        Commands.AddPermissionsToRole command = new Commands.AddPermissionsToRole(
                RoleName.from(roleName),
                request.permissions().stream().map(PermissionName::from).collect(Collectors.toSet()),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.addPermissionsToRole(command);
    }

    @DeleteMapping("/{roleName}/permissions")
    @RolesAllowed({"admin", "auth:role:delete"})
    public void removePermissionsFromRole(@PathVariable String roleName,
                                                       @Valid @RequestBody Requests.RemovePermissionsFromRole request) {
        Commands.RemovePermissionsFromRole removePermissionsFromRole = new Commands.RemovePermissionsFromRole(
                RoleName.from(roleName),
                request.permissions().stream().map(PermissionName::from).collect(Collectors.toSet()),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.removePermissionsFromRole(removePermissionsFromRole);
    }

    @PostMapping("{roleName}/assign")
    @RolesAllowed({"admin", "auth:user:update"})
    public void assignRoleToUsers(@PathVariable String roleName,
                                               @Valid @RequestBody Requests.AssignRoleToUsers request) {
        Commands.AssignRoleToUsers assignRoleToUsers = new Commands.AssignRoleToUsers(
                RoleName.from(roleName),
                request.users().stream().map(Username::of).collect(Collectors.toSet()),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.assignRoleToUsers(assignRoleToUsers);
    }

    @DeleteMapping("/{roleName}")
    @RolesAllowed({"admin", "auth:role:delete"})
    public void deleteRole(@PathVariable String roleName) {
        Commands.DeleteRole deleteRole = new Commands.DeleteRole(
                RoleName.from(roleName),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.deleteRole(deleteRole);
    }
}
