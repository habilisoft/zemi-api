package org.habilisoft.zemi.sales.customer.domain;

public record CustomerAddressChanged(CustomerId customerId, Address newAddress) { }