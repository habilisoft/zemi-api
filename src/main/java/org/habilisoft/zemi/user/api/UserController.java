package org.habilisoft.zemi.user.api;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.user.UserService;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.usecase.Commands;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({"admin", "auth:user:create"})
    public void createUser(@Valid @RequestBody Requests.CreateUser request) {
        Commands.CreateUser command = new Commands.CreateUser(
                request.username(),
                request.name(),
                request.password(),
                request.changePasswordAtNextLogin(),
                request.roles(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.createUser(command);
    }

    @GetMapping("/me")
    public ResponseEntity<Responses.User> me(Principal principal) {
        return Optional.ofNullable(principal)
                .map(p -> {
                    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) p;
                    User user = (User) auth.getPrincipal();
                    List<String> permissions = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();
                    Responses.User response = new Responses.User(
                            user.getUsername(),
                            user.getName(),
                            user.getChangePasswordAtNextLogin(),
                            permissions
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody Requests.ChangePassword request) {
        Commands.ChangePassword command = new Commands.ChangePassword(
                Username.of(userService.getCurrentUser()),
                request.currentPassword(),
                request.newPassword(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.changePassword(command);
    }

    @RolesAllowed({"admin", "auth:user:reset-password"})
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@Valid @RequestBody Requests.ResetPassword request) {
        Commands.ResetPassword command = new Commands.ResetPassword(
                request.username(),
                request.password(),
                request.changePasswordAtNextLogin(),
                userService.getCurrentUser(),
                LocalDateTime.now()
        );
        userService.resetPassword(command);
    }


    @RolesAllowed({"admin", "auth:user:edit"})
    @PostMapping("{username}/roles")
    @ResponseStatus(HttpStatus.OK)
    public void addRoleToUser(@PathVariable Username username,
                              @Valid @RequestBody Requests.AddRolesToUser request) {
        userService.addRolesToUser(
                new Commands.AddRolesToUser(
                        username,
                        request.roles(),
                        userService.getCurrentUser(),
                        LocalDateTime.now()
                )
        );
    }

    @RolesAllowed({"admin", "auth:user:edit"})
    @PostMapping("{username}/roles/remove")
    @ResponseStatus(HttpStatus.OK)
    public void removeRolesFromUser(@PathVariable Username username,
                                    @Valid @RequestBody Requests.RemoveRolesFromUser request) {
        userService.removeRoleFromUser(
                new Commands.RemoveRolesFromUser(
                        username,
                        request.roles(),
                        userService.getCurrentUser(),
                        LocalDateTime.now()
                )
        );
    }

    @RolesAllowed({"admin", "auth:user:delete"})
    @DeleteMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Username username) {
        userService.deleteUser(
                new Commands.DeleteUser(
                        username,
                        userService.getCurrentUser(),
                        LocalDateTime.now()
                )
        );
    }

}
