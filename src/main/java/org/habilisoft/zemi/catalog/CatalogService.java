package org.habilisoft.zemi.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final RegisterProductUseCase registerProductUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;

    public ProductId registerProduct(RegisterProduct registerProduct) {
        return registerProductUseCase.execute(registerProduct);
    }
    public CategoryId createCategory(CreateCategory createCategory) {
        return createCategoryUseCase.execute(createCategory);
    }
}
