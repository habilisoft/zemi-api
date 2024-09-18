package org.habilisoft.zemi.taxesmanagement.ncf.application;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfType;

import java.time.LocalDateTime;

public sealed interface GenerateNcf extends Command {
    record ForCustomer(CustomerId customerId, LocalDateTime time, String user) implements GenerateNcf { }
    record ForType(NcfType ncfType, LocalDateTime time, String user) implements GenerateNcf { }
}
