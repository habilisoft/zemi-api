package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.AssignRoleToUsers;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.exception.RoleNotFoundException;
import org.habilisoft.zemi.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AssignRoleToUsersUseCase implements UseCase<AssignRoleToUsers, Void> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Void execute(AssignRoleToUsers assignRoleToUsers) {
        RoleName roleName = assignRoleToUsers.roleName();
        if (!roleRepository.existsById(roleName)) {
            throw new RoleNotFoundException(roleName);
        }
        Map<Username, User> userMap = userRepository.findAllById(assignRoleToUsers.usernames())
                .stream().collect(Collectors.toMap(User::getUsername, Function.identity()));
        LocalDateTime time = assignRoleToUsers.time();
        assignRoleToUsers.usernames().forEach(username -> {
            User user = Optional.ofNullable(userMap.get(username))
                    .orElseThrow(() -> new UserNotFoundException(username));
            user.addRoles(Set.of(roleName), time, Username.of(assignRoleToUsers.user()));
        });
        Collection<User> users = userMap.values();
        userRepository.saveAll(users);
        return null;
    }
}
