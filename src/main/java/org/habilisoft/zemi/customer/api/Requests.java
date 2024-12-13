package org.habilisoft.zemi.customer.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.habilisoft.zemi.customer.domain.*;

import java.util.Set;

interface Requests {
    record RegisterCustomer(
            @NotNull @NotBlank String name,
            CustomerType type,
            Set<PhoneNumber> phoneNumbers,
            EmailAddress emailAddress,
            Address address
    ) {

        Contact contact() {
            return new Contact(phoneNumbers, emailAddress);
        }
    }
}
