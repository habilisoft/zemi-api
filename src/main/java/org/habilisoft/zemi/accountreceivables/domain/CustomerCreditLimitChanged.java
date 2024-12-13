package org.habilisoft.zemi.accountreceivables.domain;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;

public record CustomerCreditLimitChanged(CustomerId customerId, MonetaryAmount newCreditLimit) { }
