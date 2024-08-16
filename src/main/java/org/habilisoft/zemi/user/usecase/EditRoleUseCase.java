package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.command.EditRole;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.exception.RoleNotFoundException;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.service.PermissionService;

import java.util.Set;

@RequiredArgsConstructor
public class EditRoleUseCase implements UseCase<EditRole, Void> {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public Void execute(EditRole editRole) {
        RoleName roleName = editRole.roleName();
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
        Set<PermissionName> permissions = editRole.permissions();
        permissionService.ensureRoleExists(permissions);
        role.setPermissions(permissions);
        roleRepository.save(role);
        return null;
    }
}
