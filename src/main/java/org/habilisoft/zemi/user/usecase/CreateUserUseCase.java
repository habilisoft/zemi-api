package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.CreateUser;
import org.habilisoft.zemi.user.domain.*;
import org.habilisoft.zemi.user.exception.RoleNotFoundException;
import org.habilisoft.zemi.user.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateUserUseCase implements UseCase<CreateUser, Void> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Void execute(CreateUser createUser) {
        userRepository.findById(createUser.username())
                .ifPresent(_ -> {
                    throw new UserAlreadyExistsException(createUser.username());
                });
        String encodedPassword = passwordEncoder.encode(createUser.password());
        Map<RoleName, Role> roleMap = roleRepository.findAllById(createUser.roles())
                .stream().collect(Collectors.toMap(Role::getName, Function.identity()));

        createUser.roles().forEach(roleName -> {
            if (!roleMap.containsKey(roleName)) {
                throw new RoleNotFoundException(roleName);
            }
        });
        Username username = Username.of(createUser.user());
        LocalDateTime time = createUser.time();
        User user = User.create(createUser.username(), createUser.name(), encodedPassword, createUser.changePasswordAtNextLogin(), createUser.roles(), time, username);
        userRepository.save(user);
        return null;
    }
}
