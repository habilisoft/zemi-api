package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.IdempotentUseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.DeleteRole;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.exception.CantDeleteSystemRoleException;
import org.habilisoft.zemi.user.exception.RoleInUseException;
import org.habilisoft.zemi.user.exception.RoleNotFoundException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DeleteRoleUseCase implements IdempotentUseCase<DeleteRole, Void> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Void execute(DeleteRole deleteRole) {
        RoleName roleName = deleteRole.roleName();
        Role role = roleRepository.findById(roleName).orElseThrow(() -> new RoleNotFoundException(roleName));
        if (role.isSystemRole()) {
            throw new CantDeleteSystemRoleException();
        }
        int userCount = userRepository.countUsersByRole(roleName);
        if (userCount > 0) {
            throw new RoleInUseException(roleName, userCount);
        }
        Username username = Username.of(deleteRole.user());
        LocalDateTime time = deleteRole.time();
        role.delete(time, username);
        roleRepository.save(role);
        return null;
    }

    @Override
    public String idempotencyKey(DeleteRole deleteRole) {
        return deleteRole.roleName().value();
    }
}
