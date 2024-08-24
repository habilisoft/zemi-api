package org.habilisoft.zemi.catalog.product.application;

import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;
import java.util.Optional;

public record RegisterProduct(Optional<CategoryId> categoryId,
                              String name,
                              boolean isService,
                              LocalDateTime time, String user) implements Command { }
