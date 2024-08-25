package org.habilisoft.zemi.taxesmanagement.customer.domain;

import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.taxesmanagement.ncf.NcfType;

public record CustomerNcfTypeChanged(CustomerId customerId, NcfType ncfType) {}
