package org.habilisoft.zemi.pricemanagement.pricelist.application;

import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record UpdatePriceList(PriceListId priceListId, String name, LocalDateTime time, String user) implements Command {
}
