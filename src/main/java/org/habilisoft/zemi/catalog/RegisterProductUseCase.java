package org.habilisoft.zemi.catalog;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterProductUseCase implements UseCase<RegisterProduct, ProductId> {
    private final ProductIdGenerator productIdGenerator;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductId execute(RegisterProduct registerProduct) {
        String name = registerProduct.name();
        ProductId productId = productIdGenerator.generate();
        Product product = Product.register(productId, name);
        registerProduct.categoryId()
                .filter(this::ensureCategoryExists)
                .ifPresent(product::setCategoryId);
        productRepository.save(product);
        return productId;
    }

    private boolean ensureCategoryExists(CategoryId categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFound(categoryId);
        }
        return true;
    }
}

