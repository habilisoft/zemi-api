package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddRolesToUserUseCase implements UseCase<UserCommands.AddRolesToUser, Boolean> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Boolean execute(UserCommands.AddRolesToUser addRolesToUser) {
        User user = userRepository.findById(addRolesToUser.username())
                .orElseThrow(() -> new Exceptions.UserNotFound(addRolesToUser.username()));
        Set<RoleName> roles = addRolesToUser.roles();
        Map<RoleName, Role> roleMap = roleRepository.findAllById(roles)
                .stream().collect(Collectors.toMap(Role::getName, Function.identity()));

        roles.forEach(roleName -> {
            if (!roleMap.containsKey(roleName)) {
                throw new Exceptions.RoleNotFound(roleName);
            }
        });
        Username username = Username.of(addRolesToUser.user());
        LocalDateTime time = addRolesToUser.time();
        user.addRoles(roles, time, username);
        userRepository.save(user);
        return true;
    }
}
