package org.habilisoft.zemi.catalog.category.application;

import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record UpdateCategory(CategoryId categoryId, String name, LocalDateTime time, String user) implements Command {}
