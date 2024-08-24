package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class RemoveRoleFromUserUseCase implements UseCase<UserCommands.RemoveRolesFromUser, Void> {
    private final UserRepository userRepository;

    @Override
    public Void execute(UserCommands.RemoveRolesFromUser removeRolesFromUser) {
        User user = userRepository.findById(removeRolesFromUser.username())
                .orElseThrow(() -> new Exceptions.UserNotFound(removeRolesFromUser.username()));
        Username username = Username.of(removeRolesFromUser.user());
        LocalDateTime time = removeRolesFromUser.time();
        user.removeRoles(removeRolesFromUser.roles(), time, username);
        userRepository.save(user);
        return null;
    }
}
