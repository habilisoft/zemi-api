package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AssignRoleToUsersUseCase implements UseCase<UserCommands.AssignRoleToUsers, Void> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Void execute(UserCommands.AssignRoleToUsers assignRoleToUsers) {
        RoleName roleName = assignRoleToUsers.roleName();
        if (!roleRepository.existsById(roleName)) {
            throw new Exceptions.RoleNotFound(roleName);
        }
        Map<Username, User> userMap = userRepository.findAllById(assignRoleToUsers.usernames())
                .stream().collect(Collectors.toMap(User::getUsername, Function.identity()));
        LocalDateTime time = assignRoleToUsers.time();
        assignRoleToUsers.usernames().forEach(username -> {
            User user = Optional.ofNullable(userMap.get(username))
                    .orElseThrow(() -> new Exceptions.UserNotFound(username));
            user.addRoles(Set.of(roleName), time, Username.of(assignRoleToUsers.user()));
        });
        Collection<User> users = userMap.values();
        userRepository.saveAll(users);
        return null;
    }
}
