package org.habilisoft.zemi.catalog.product.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.springframework.modulith.NamedInterface;

@Getter
@NamedInterface
public class ProductNotFound extends DomainException {
    private final ProductId productId;

    public ProductNotFound(ProductId productId) {
        super("Product %s not found".formatted(productId.value()));
        this.productId = productId;
    }
}
