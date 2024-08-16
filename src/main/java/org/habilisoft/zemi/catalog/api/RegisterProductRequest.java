package org.habilisoft.zemi.catalog.api;

import jakarta.validation.constraints.NotBlank;

public record RegisterProductRequest(
        Long categoryId, @NotBlank String name
) { }
