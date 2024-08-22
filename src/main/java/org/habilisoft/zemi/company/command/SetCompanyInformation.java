package org.habilisoft.zemi.company.command;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.user.domain.User;

import java.time.LocalDateTime;
import java.util.Optional;

public record SetCompanyInformation(String name,
                             Optional<String> address,
                             Optional<String> phone,
                             Optional<String> email,
                             Optional<String> website,
                             Optional<String> logo,
                             Optional<String> document,
                             String user,
                             LocalDateTime time) implements Command {
}
