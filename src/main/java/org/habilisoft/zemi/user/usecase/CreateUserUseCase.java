package org.habilisoft.zemi.user.usecase;


import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.*;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CreateUserUseCase implements UseCase<Commands.CreateUser, Void> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Void execute(Commands.CreateUser createUser) {
        userRepository.findById(createUser.username())
                .ifPresent(_ -> {
                    throw new Exceptions.UserAlreadyExists(createUser.username());
                });
        String encodedPassword = passwordEncoder.encode(createUser.password());
        Map<RoleName, Role> roleMap = roleRepository.findAllById(createUser.roles())
                .stream().collect(Collectors.toMap(Role::getName, Function.identity()));

        createUser.roles().forEach(roleName -> {
            if (!roleMap.containsKey(roleName)) {
                throw new Exceptions.RoleNotFound(roleName);
            }
        });
        Username username = Username.of(createUser.user());
        LocalDateTime time = createUser.time();
        User user = User.create(createUser.username(), createUser.name(), encodedPassword, createUser.changePasswordAtNextLogin(), createUser.roles(), time, username);
        userRepository.save(user);
        return null;
    }
}
