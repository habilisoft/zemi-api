package org.habilisoft.zemi.sales.api;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

interface Requests {
    record MakeSale(
            @NotNull String documentId,
            Long customerId,
            @NotNull Set<SaleProduct> products
    ) {
        record SaleProduct(
                @NotNull Long productId,
                BigDecimal price,
                @NotNull BigDecimal quantity
        ) {}
    }
}
