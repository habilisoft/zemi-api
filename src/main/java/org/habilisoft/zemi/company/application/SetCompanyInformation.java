package org.habilisoft.zemi.company.application;

import org.habilisoft.zemi.shared.Command;

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
