package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.habilisoft.zemi.user.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
@RequiredArgsConstructor
public class EditRoleUseCase implements UseCase<Commands.EditRole, Void> {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public Void execute(Commands.EditRole editRole) {
        RoleName roleName = editRole.roleName();
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new Exceptions.RoleNotFound(roleName));
        Set<PermissionName> permissions = editRole.permissions();
        permissionService.ensureRoleExists(permissions);
        role.setPermissions(permissions);
        roleRepository.save(role);
        return null;
    }
}
