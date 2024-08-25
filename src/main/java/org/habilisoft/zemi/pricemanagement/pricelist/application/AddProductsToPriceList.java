package org.habilisoft.zemi.pricemanagement.pricelist.application;

import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.ProductIdAndPrice;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;
import java.util.Set;

public record AddProductsToPriceList(
        PriceListId priceListId,
        Set<ProductIdAndPrice> prices,
        LocalDateTime time, String user) implements Command {
}
