package org.habilisoft.zemi.pricemanagement.application;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.shared.MonetaryAmount;

import java.time.LocalDateTime;

public record ChangeProductPrice(ProductId productId, MonetaryAmount newPrice, LocalDateTime time, String user) implements Command { }