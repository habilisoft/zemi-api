package org.habilisoft.zemi.catalog;

import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record CreateCategory(String name, LocalDateTime time, String user) implements Command { }
