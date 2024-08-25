package org.habilisoft.zemi.pricemanagement.api;

import jakarta.validation.constraints.NotNull;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.ProductIdAndPrice;
import org.habilisoft.zemi.shared.MonetaryAmount;

import java.math.BigDecimal;
import java.util.Set;

interface Requests {
    record ChangeProductPrice(BigDecimal price) { }
    record ProductAndPrice(@NotNull Long productId, @NotNull BigDecimal price) {
        ProductIdAndPrice toProductIdAndPrice() {
            return new ProductIdAndPrice(ProductId.of(productId), MonetaryAmount.of(price));
        }
    }
    record CreatePriceList(@NotNull String name, Set<ProductAndPrice> products) { }
    record RemoveProductsToPriceList(@NotNull Set<Long> products) { }
    record ClonePriceList(@NotNull String name) { }
    record ChangeProductPriceCurrentPrice(Set<ProductAndPrice> products) { }
    record AddProductsToPriceList(Set<ProductAndPrice> products) { }
}
