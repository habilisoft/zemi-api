package org.habilisoft.zemi.catalog.product.application;

import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateProduct(ProductId productId,
                            Optional<CategoryId> categoryId,
                            Optional<String> name,
                            Optional<Boolean> isService,
                            LocalDateTime time, String user) implements Command {}
