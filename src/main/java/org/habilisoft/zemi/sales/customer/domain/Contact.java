package org.habilisoft.zemi.sales.customer.domain;

import java.util.Set;

public record Contact(Set<PhoneNumber> phoneNumbers, EmailAddress emailAddress) {
    public static Contact of(Set<PhoneNumber> phoneNumbers, EmailAddress emailAddress) {
        return new Contact(phoneNumbers, emailAddress);
    }
}