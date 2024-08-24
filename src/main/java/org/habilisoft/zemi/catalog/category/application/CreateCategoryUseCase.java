package org.habilisoft.zemi.catalog.category.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.category.domain.Category;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.category.domain.CategoryRepository;
import org.habilisoft.zemi.catalog.category.domain.CreateCategoryIdGenerator;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase implements UseCase<CreateCategory, CategoryId> {
    private final CategoryRepository categoryRepository;
    private final CreateCategoryIdGenerator createCategoryIdGenerator;
    @Override
    public CategoryId execute(CreateCategory createCategory) {
        CategoryId categoryId = createCategoryIdGenerator.generate();
        LocalDateTime createdAt = createCategory.time();
        Username createdBy = Username.of(createCategory.user());
        Category category = Category.create(categoryId, createCategory.name(), createdAt, createdBy);
        categoryRepository.save(category);
        return categoryId;
    }
}
