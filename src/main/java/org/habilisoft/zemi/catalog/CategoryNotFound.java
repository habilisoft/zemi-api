package org.habilisoft.zemi.catalog;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;

@Getter
public class CategoryNotFound extends DomainException {
    private final CategoryId categoryId;

    public CategoryNotFound(CategoryId categoryId) {
        super("Category %s not found".formatted(categoryId.value()));
        this.categoryId = categoryId;
    }
}
