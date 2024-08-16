package org.habilisoft.zemi.catalog;

import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;
import java.util.Optional;

public record RegisterProduct(Optional<CategoryId> categoryId, String name, LocalDateTime time, String user) implements Command { }
