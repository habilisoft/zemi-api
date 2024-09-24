package org.habilisoft.zemi.sales.sale.domain;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;
import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;
@NamedInterface
public record SaleMade(TransactionalId transactionalId, CustomerId customerId, MonetaryAmount total, LocalDateTime time, String user) { }
