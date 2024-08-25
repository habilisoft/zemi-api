package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import jakarta.persistence.Embeddable;
import org.habilisoft.zemi.catalog.product.domain.ProductId;

import java.io.Serializable;
import java.time.LocalDateTime;
@Embeddable
public record PriceListProductId(PriceListId priceListId, ProductId productId,
                                 LocalDateTime date) implements Serializable { }
