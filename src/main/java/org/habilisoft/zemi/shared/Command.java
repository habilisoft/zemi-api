package org.habilisoft.zemi.shared;
import java.time.LocalDateTime;

public interface Command {
    String user();
    LocalDateTime time();
}
