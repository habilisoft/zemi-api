package org.habilisoft.zemi.catalog.api;

import jakarta.validation.constraints.NotBlank;
import org.habilisoft.zemi.catalog.CategoryId;

interface Requests {
    record RegisterProduct(CategoryId categoryId, @NotBlank String name) { }
    record CreateCategory(@NotBlank String name) { }
}
