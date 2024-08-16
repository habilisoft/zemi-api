package org.habilisoft.zemi.user.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.habilisoft.zemi.user.domain.Exceptions;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetPasswordUseCase implements UseCase<Commands.ResetPassword, Void> {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Void execute(Commands.ResetPassword resetPassword) {
        User user = userRepository.findById(resetPassword.username())
                .orElseThrow(() -> new Exceptions.UserNotFound(resetPassword.username()));
        Username username = Username.of(resetPassword.user());
        LocalDateTime time = resetPassword.time();

        String encodedPassword = passwordEncoder.encode(resetPassword.password());
        user.setPassword(encodedPassword, resetPassword.changePasswordAtNextLogin(), time, username);
        userRepository.save(user);
        return null;
    }
}
