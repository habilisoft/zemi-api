package org.habilisoft.zemi.pricemanagement.customer.application;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record ChangeCustomerPriceList(CustomerId customerId, PriceListId priceListId, LocalDateTime time, String user) implements Command {
}
