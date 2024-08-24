package org.habilisoft.zemi.catalog.product.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.category.domain.CategoryNotFound;
import org.habilisoft.zemi.catalog.category.domain.CategoryRepository;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductIdGenerator;
import org.habilisoft.zemi.catalog.product.domain.ProductRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterProductUseCase implements UseCase<RegisterProduct, ProductId> {
    private final ProductIdGenerator productIdGenerator;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductId execute(RegisterProduct registerProduct) {
        ProductId productId = productIdGenerator.generate();
        Product product = registerProduct(productId, registerProduct);
        registerProduct.categoryId()
                .filter(this::ensureCategoryExists)
                .ifPresent(product::setCategoryId);
        productRepository.save(product);
        return productId;
    }

    private static Product registerProduct(ProductId productId, RegisterProduct registerProduct) {
        String name = registerProduct.name();
        LocalDateTime createdAt = registerProduct.time();
        Username createdBy = Username.of(registerProduct.user());
        if (registerProduct.isService()) {
            return Product.registerService(productId, name, createdAt, createdBy);
        }
        return Product.registerProduct(productId, name, createdAt, createdBy);
    }

    private boolean ensureCategoryExists(CategoryId categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFound(categoryId);
        }
        return true;
    }
}

