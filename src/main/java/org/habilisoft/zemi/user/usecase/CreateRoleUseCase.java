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
public class CreateRoleUseCase implements UseCase<Commands.CreateRole, Void> {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public Void execute(Commands.CreateRole createRole) {
        RoleName roleName = createRole.name();
        String description = createRole.description();
        Set<PermissionName> permissions = createRole.permissions();
        roleRepository.findById(roleName)
                .ifPresent(_ -> {
                    throw new Exceptions.RoleAlreadyExists(roleName);
                });
        permissionService.ensureRoleExists(permissions);
        if (permissions.isEmpty()) {
            throw new Exceptions.AtLeastOnePermissionRequired();
        }
        Username username = Username.of(createRole.user());
        LocalDateTime time = createRole.time();
        Role role = Role.create(roleName, description, permissions, time, username);
        roleRepository.save(role);
        return null;
    }
}
