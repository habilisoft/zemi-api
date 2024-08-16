package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.AddRolesToUser;
import org.habilisoft.zemi.user.domain.*;
import org.habilisoft.zemi.user.exception.RoleNotFoundException;
import org.habilisoft.zemi.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class AddRolesToUserUseCase implements UseCase<AddRolesToUser, Boolean> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Boolean execute(AddRolesToUser addRolesToUser) {
        User user = userRepository.findById(addRolesToUser.username())
                .orElseThrow(() -> new UserNotFoundException(addRolesToUser.username()));
        Set<RoleName> roles = addRolesToUser.roles();
        Map<RoleName, Role> roleMap = roleRepository.findAllById(roles)
                .stream().collect(Collectors.toMap(Role::getName, Function.identity()));

        roles.forEach(roleName -> {
            if (!roleMap.containsKey(roleName)) {
                throw new RoleNotFoundException(roleName);
            }
        });
        Username username = Username.of(addRolesToUser.user());
        LocalDateTime time = addRolesToUser.time();
        user.addRoles(roles, time, username);
        userRepository.save(user);
        return true;
    }
}
