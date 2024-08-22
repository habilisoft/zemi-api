package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase implements UseCase<Commands.ChangePassword, Void> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Void execute(Commands.ChangePassword command) {
        User user = userRepository.findById(command.username())
                .orElseThrow(() -> new Exceptions.UserNotFound(command.username()));
        if (!passwordEncoder.matches(command.oldPassword(), user.getPassword())) {
            throw new Exceptions.InvalidOldPassword();
        }
        user.setPassword(passwordEncoder.encode(command.newPassword()));
        user.setChangePasswordAtNextLogin(false);
        userRepository.save(user);
        return null;
    }
}
