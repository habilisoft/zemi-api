package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase implements UseCase<UserCommands.DeleteUser, Void> {
    private final UserRepository userRepository;

    @Override
    public Void execute(UserCommands.DeleteUser deleteUser) {
        User user = userRepository.findById(deleteUser.username())
                .orElseThrow(() -> new Exceptions.UserNotFound(deleteUser.username()));
        if (Objects.equals(user.getUsername(), Username.of(deleteUser.user()))) {
            throw new Exceptions.UserCannotDeleteHimself();
        }
        Username username = Username.of(deleteUser.user());
        LocalDateTime time = deleteUser.time();
        user.delete(time, username);
        userRepository.save(user);
        return null;
    }
}
