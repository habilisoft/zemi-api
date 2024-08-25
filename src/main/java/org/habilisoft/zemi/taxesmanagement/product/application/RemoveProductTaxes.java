package org.habilisoft.zemi.taxesmanagement.product.application;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;

import java.time.LocalDateTime;
import java.util.Set;

public record RemoveProductTaxes(ProductId productId, Set<TaxId> taxIds, LocalDateTime time, String user) implements Command { }