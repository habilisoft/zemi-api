package org.habilisoft.zemi.pricemanagement.product.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductNotFound;
import org.habilisoft.zemi.pricemanagement.product.domain.Product;
import org.habilisoft.zemi.pricemanagement.product.domain.ProductPriceRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeProductPriceUseCase implements UseCase<ChangeProductPrice, Void> {
    private final ProductPriceRepository productPriceRepository;

    @Override
    public Void execute(ChangeProductPrice changeProductPrice) {
        ProductId productId = changeProductPrice.productId();
        Product product = productPriceRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFound(productId));
        product.changePrice(changeProductPrice.newPrice(), changeProductPrice.time(), Username.of(changeProductPrice.user()));
        productPriceRepository.save(product);
        return null;
    }
}
