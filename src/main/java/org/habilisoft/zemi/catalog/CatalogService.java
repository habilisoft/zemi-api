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
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final RegisterProductUseCase registerProductUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ProductRepository productRepository;

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

    public Map<ProductId, Boolean> productExists(Set<ProductId> productIds) {
        Map<ProductId, Boolean> productMap = productRepository.findAllById(productIds)
                .stream().collect(Collectors.toMap(Product::getId, _ -> true));
        productIds.forEach(id -> productMap.putIfAbsent(id, false));
        return productMap;
    }
}
