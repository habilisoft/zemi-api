package org.habilisoft.zemi.pricemanagement.pricelist.application;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;
import java.util.Set;

public record RemoveProductsToPriceList(PriceListId priceListId,
                                        Set<ProductId> products,
                                        LocalDateTime time, String user) implements Command {
}
