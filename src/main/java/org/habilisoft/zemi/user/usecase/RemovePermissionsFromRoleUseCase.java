package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.RemovePermissionsFromRole;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.exception.RoleNotFoundException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RemovePermissionsFromRoleUseCase implements UseCase<RemovePermissionsFromRole, Void> {
    private final RoleRepository roleRepository;
    @Override
    public Void execute(RemovePermissionsFromRole removePermissionsFromRole) {
        RoleName roleName = removePermissionsFromRole.roleName();
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
        Username username = Username.of(removePermissionsFromRole.user());
        LocalDateTime time = removePermissionsFromRole.time();
        role.removePermissions(removePermissionsFromRole.permissions(), time, username);
        roleRepository.save(role);
        return null;
    }
}
