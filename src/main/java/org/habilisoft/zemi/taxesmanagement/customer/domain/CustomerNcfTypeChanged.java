package org.habilisoft.zemi.taxesmanagement.customer.domain;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfType;

public record CustomerNcfTypeChanged(CustomerId customerId, NcfType ncfType) {}
