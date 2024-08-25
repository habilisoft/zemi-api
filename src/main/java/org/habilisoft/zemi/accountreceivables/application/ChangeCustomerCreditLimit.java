package org.habilisoft.zemi.accountreceivables.application;

import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.shared.MonetaryAmount;

import java.time.LocalDateTime;

public record ChangeCustomerCreditLimit(CustomerId customerId, MonetaryAmount newCreditLimit, LocalDateTime time, String user) implements Command { }