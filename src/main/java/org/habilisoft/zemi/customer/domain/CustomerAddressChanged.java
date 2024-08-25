package org.habilisoft.zemi.customer.domain;

public record CustomerAddressChanged(CustomerId customerId, Address newAddress) { }