package org.habilisoft.zemi.user.service;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.user.domain.Permission;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.PermissionRepository;
import org.habilisoft.zemi.user.exception.PermissionNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public void ensureRoleExists(Set<PermissionName> permissions) {
        Map<PermissionName, Permission> permissionExistence = permissionRepository.findAllById(permissions)
                .stream().collect(Collectors.toMap(Permission::getName, Function.identity()));

        permissions.stream().filter(permissionId -> !permissionExistence.containsKey(permissionId))
                .findAny().ifPresent(permissionId -> {
                    throw new PermissionNotFoundException(permissionId);
                });
    }
}
