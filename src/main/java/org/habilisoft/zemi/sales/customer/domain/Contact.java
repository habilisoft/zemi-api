package org.habilisoft.zemi.sales.customer.domain;

import java.util.Set;

public record Contact(Set<PhoneNumber> phoneNumbers, EmailAddress emailAddress) { }