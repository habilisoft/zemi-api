package org.habilisoft.zemi.sales.sale.domain;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;

import java.time.LocalDateTime;

public record SaleMade(TransactionalId transactionalId, CustomerId customerId, MonetaryAmount total, LocalDateTime time, String user) { }
