package org.habilisoft.zemi.sales.sale.application;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.shared.DocumentId;
import org.habilisoft.zemi.shared.Quantity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public record MakeSale(DocumentId documentId, Optional<CustomerId> customerId, Set<Product> products, LocalDateTime time, String user) implements Command {
    public record Product(ProductId productId, Quantity quantity) { }
}
