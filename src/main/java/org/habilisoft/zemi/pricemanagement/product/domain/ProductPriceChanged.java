package org.habilisoft.zemi.pricemanagement.product.domain;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;

import java.util.Optional;

public record ProductPriceChanged(ProductId productId, Optional<MonetaryAmount> oldPrice, MonetaryAmount newPrice) { }
