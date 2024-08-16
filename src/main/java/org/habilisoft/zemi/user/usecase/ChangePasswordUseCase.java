package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.command.ChangePassword;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.exception.InvalidOldPasswordException;
import org.habilisoft.zemi.user.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
public class ChangePasswordUseCase implements UseCase<ChangePassword, Void> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Void execute(ChangePassword command) {
        User user = userRepository.findById(command.username())
                .orElseThrow(() -> new UserNotFoundException(command.username()));
        if (!passwordEncoder.matches(command.oldPassword(), user.getPassword())) {
            throw new InvalidOldPasswordException();
        }
        user.setPassword(passwordEncoder.encode(command.newPassword()));
        userRepository.save(user);
        return null;
    }
}
