package org.habilisoft.zemi.catalog.product.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.category.domain.CategoryNotFound;
import org.habilisoft.zemi.catalog.category.domain.CategoryRepository;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductNotFound;
import org.habilisoft.zemi.catalog.product.domain.ProductRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCase implements UseCase<UpdateProduct, Void> {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Void execute(UpdateProduct updateProduct) {
        ProductId productId = updateProduct.productId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFound(productId));
        LocalDateTime updatedAt = updateProduct.time();
        Username updatedBy = Username.of(updateProduct.user());
        product.update(updatedAt, updatedBy);
        updateProduct.name().ifPresent(product::setName);
        updateProduct.isService().ifPresent(product::setService);
        updateProduct.categoryId()
                .filter(categoryId -> {
                    if (!categoryRepository.existsById(categoryId)) {
                        throw new CategoryNotFound(categoryId);
                    }
                    return true;
                }).ifPresent(product::setCategoryId);
        productRepository.save(product);
        return null;
    }
}

