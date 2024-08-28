package org.habilisoft.zemi.pricemanagement.customer.application;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record InitializeCustomerPriceList(CustomerId customerId, LocalDateTime time, String user) implements Command {
}
