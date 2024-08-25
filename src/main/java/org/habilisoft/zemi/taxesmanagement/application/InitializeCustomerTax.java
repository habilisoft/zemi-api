package org.habilisoft.zemi.taxesmanagement.application;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record InitializeCustomerTax(CustomerId customerId, LocalDateTime time, String user) implements Command { }
