package org.habilisoft.zemi.accountreceivables;

import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;

public record CustomerCreditLimitUpdated(CustomerId customerId, MonetaryAmount newCreditLimit) { }
