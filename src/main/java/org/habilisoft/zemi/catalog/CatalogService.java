package org.habilisoft.zemi.catalog;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.category.application.CreateCategory;
import org.habilisoft.zemi.catalog.category.application.CreateCategoryUseCase;
import org.habilisoft.zemi.catalog.category.application.UpdateCategory;
import org.habilisoft.zemi.catalog.category.application.UpdateCategoryUseCase;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.product.application.RegisterProduct;
import org.habilisoft.zemi.catalog.product.application.RegisterProductUseCase;
import org.habilisoft.zemi.catalog.product.application.UpdateProduct;
import org.habilisoft.zemi.catalog.product.application.UpdateProductUseCase;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final RegisterProductUseCase registerProductUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final UpdateProductUseCase updateProductUseCase;

    public ProductId registerProduct(RegisterProduct registerProduct) {
        return registerProductUseCase.execute(registerProduct);
    }
    public CategoryId createCategory(CreateCategory createCategory) {
        return createCategoryUseCase.execute(createCategory);
    }
    public void updateCategory(UpdateCategory updateCategory) {
        updateCategoryUseCase.execute(updateCategory);
    }

    public void updateProduct(UpdateProduct updateProduct) {
        updateProductUseCase.execute(updateProduct);
    }
}
