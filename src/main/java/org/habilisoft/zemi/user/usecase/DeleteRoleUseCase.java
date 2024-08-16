package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class DeleteRoleUseCase implements UseCase<Commands.DeleteRole, Void> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Void execute(Commands.DeleteRole deleteRole) {
        RoleName roleName = deleteRole.roleName();
        Role role = roleRepository.findById(roleName).orElseThrow(() -> new Exceptions.RoleNotFound(roleName));
        if (role.isSystemRole()) {
            throw new Exceptions.CantDeleteSystemRole();
        }
        int userCount = userRepository.countUsersByRole(roleName);
        if (userCount > 0) {
            throw new Exceptions.RoleInUse(roleName, userCount);
        }
        Username username = Username.of(deleteRole.user());
        LocalDateTime time = deleteRole.time();
        role.delete(time, username);
        roleRepository.save(role);
        return null;
    }
}
