package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.CreateRole;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.exception.AtLeastOnePermissionRequiredException;
import org.habilisoft.zemi.user.exception.RoleAlreadyExistsException;
import org.habilisoft.zemi.user.service.PermissionService;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
public class CreateRoleUseCase implements UseCase<CreateRole, Void> {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public Void execute(CreateRole createRole) {
        RoleName roleName = createRole.name();
        String description = createRole.description();
        Set<PermissionName> permissions = createRole.permissions();
        AuditableProperties auditableProperties = AuditableProperties.of(createRole.time(), Username.of(createRole.user()));
        roleRepository.findById(roleName)
                .ifPresent(_ -> {
                    throw new RoleAlreadyExistsException(roleName);
                });
        permissionService.ensureRoleExists(permissions);
        if (permissions.isEmpty()) {
            throw new AtLeastOnePermissionRequiredException();
        }
        Username username = Username.of(createRole.user());
        LocalDateTime time = createRole.time();
        Role role = Role.create(roleName, description, permissions, time, username);
        roleRepository.save(role);
        return null;
    }
}
