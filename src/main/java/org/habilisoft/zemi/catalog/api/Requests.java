package org.habilisoft.zemi.catalog.api;

import jakarta.validation.constraints.NotBlank;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;

import java.util.Optional;

interface Requests {
    record RegisterProduct(CategoryId categoryId, @NotBlank String name, boolean isService) { }
    record UpdateProduct(CategoryId categoryId, @NotBlank String name, Boolean isService) {
        Optional<CategoryId> getCategoryId() {
            return Optional.ofNullable(categoryId);
        }
        Optional<String> getName() {
            return Optional.ofNullable(name);
        }
        Optional<Boolean> getIsService() {
            return Optional.ofNullable(isService);
        }
    }
    record CreateCategory(@NotBlank String name) { }
    record UpdateCategory(@NotBlank String name) { }
}
