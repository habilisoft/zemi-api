package org.habilisoft.zemi.taxesmanagement;

import org.habilisoft.zemi.sales.customer.domain.CustomerId;

public record CustomerNcfTypeChanged(CustomerId customerId, NcfType ncfType) {}
