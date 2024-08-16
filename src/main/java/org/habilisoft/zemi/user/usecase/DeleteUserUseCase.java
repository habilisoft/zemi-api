package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.command.DeleteUser;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.exception.UserCannotDeleteHimselfException;
import org.habilisoft.zemi.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Objects;


@RequiredArgsConstructor
public class DeleteUserUseCase implements UseCase<DeleteUser, Void> {
    private final UserRepository userRepository;

    @Override
    public Void execute(DeleteUser deleteUser) {
        User user = userRepository.findById(deleteUser.username())
                .orElseThrow(() -> new UserNotFoundException(deleteUser.username()));
        if (Objects.equals(user.getUsername(), Username.of(deleteUser.user()))) {
            throw new UserCannotDeleteHimselfException();
        }
        Username username = Username.of(deleteUser.user());
        LocalDateTime time = deleteUser.time();
        user.delete(time, username);
        userRepository.save(user);
        return null;
    }
}
