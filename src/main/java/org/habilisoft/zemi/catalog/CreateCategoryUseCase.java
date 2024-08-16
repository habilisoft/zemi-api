package org.habilisoft.zemi.catalog;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;

@RequiredArgsConstructor
class CreateCategoryUseCase implements UseCase<CreateCategory, CategoryId> {
    private final CategoryRepository categoryRepository;
    private final CreateCategoryIdGenerator createCategoryIdGenerator;
    @Override
    public CategoryId execute(CreateCategory createCategory) {
        CategoryId categoryId = createCategoryIdGenerator.generate();
        Category category = Category.create(categoryId, createCategory.name());
        categoryRepository.save(category);
        return categoryId;
    }
}
