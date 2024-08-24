package org.habilisoft.zemi.sales.customer.domain;

import org.habilisoft.zemi.shared.MonetaryAmount;

public record CustomerCreditLimitUpdated(CustomerId customerId, MonetaryAmount newCreditLimit) { }
