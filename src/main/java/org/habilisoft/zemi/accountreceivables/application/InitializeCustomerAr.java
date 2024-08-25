package org.habilisoft.zemi.accountreceivables.application;

import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record InitializeCustomerAr(
        CustomerId customerId, LocalDateTime time, String user) implements Command { }
