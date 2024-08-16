package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.habilisoft.zemi.user.service.PermissionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class AddPermissionsToRoleUseCase implements UseCase<Commands.AddPermissionsToRole, Void> {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public Void execute(Commands.AddPermissionsToRole addPermissionsToRole) {
        RoleName roleName = addPermissionsToRole.roleName();
        Set<PermissionName> permissions = addPermissionsToRole.permissions();
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new Exceptions.RoleNotFound(roleName));
        permissionService.ensureRoleExists(permissions);
        Username username = Username.of(addPermissionsToRole.user());
        LocalDateTime time = addPermissionsToRole.time();
        role.addPermissions(permissions, time, username);
        roleRepository.save(role);
        return null;
    }
}
