package org.habilisoft.zemi.catalog.category.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.category.domain.Category;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.category.domain.CategoryNotFound;
import org.habilisoft.zemi.catalog.category.domain.CategoryRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCase implements UseCase<UpdateCategory, Void> {
    private final CategoryRepository categoryRepository;

    @Override
    public Void execute(UpdateCategory updateCategory) {
        CategoryId categoryId = updateCategory.categoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFound(categoryId));
        LocalDateTime updatedAt = updateCategory.time();
        Username updatedBy = Username.of(updateCategory.user());
        category.update(updateCategory.name(), updatedAt, updatedBy);
        categoryRepository.save(category);
        return null;
    }
}
