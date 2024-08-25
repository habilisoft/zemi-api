package org.habilisoft.zemi.taxesmanagement.application;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.taxesmanagement.ncf.NcfType;

import java.time.LocalDateTime;

public record ChangeCustomerNcfType(CustomerId customerId, NcfType ncfType, LocalDateTime time, String user) implements Command { }
