package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;

public record ProductIdAndPrice(ProductId productId, MonetaryAmount price) { }
