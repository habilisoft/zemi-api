package org.habilisoft.zemi.catalog.api;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank String name
) { }
