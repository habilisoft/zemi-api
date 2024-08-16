package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.RemoveRolesFromUser;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.exception.UserNotFoundException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RemoveRoleFromUserUseCase implements UseCase<RemoveRolesFromUser, Void> {
    private final UserRepository userRepository;

    @Override
    public Void execute(RemoveRolesFromUser removeRolesFromUser) {
        User user = userRepository.findById(removeRolesFromUser.username())
                .orElseThrow(() -> new UserNotFoundException(removeRolesFromUser.username()));
        Username username = Username.of(removeRolesFromUser.user());
        LocalDateTime time = removeRolesFromUser.time();
        user.removeRoles(removeRolesFromUser.roles(), time, username);
        userRepository.save(user);
        return null;
    }
}
