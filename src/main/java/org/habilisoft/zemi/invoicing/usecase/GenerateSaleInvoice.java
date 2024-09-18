package org.habilisoft.zemi.invoicing.usecase;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;

import java.time.LocalDateTime;

public record GenerateSaleInvoice(TransactionalId transactionalId, CustomerId customerId, MonetaryAmount total, LocalDateTime time, String user) implements Command { }
