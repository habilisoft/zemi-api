package org.habilisoft.zemi.sales.customer.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.habilisoft.zemi.sales.customer.domain.*;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.taxesmanagement.NcfType;

import java.util.Optional;
import java.util.Set;

interface Requests {
    record RegisterCustomer(
            @NotNull @NotBlank String name,
            CustomerType type,
            Set<PhoneNumber> phoneNumbers,
            EmailAddress emailAddress,
            Address address,
            Optional<NcfType> ncfType,
            Optional<MonetaryAmount> creditLimit
    ) {

        Contact contact() {
            return new Contact(phoneNumbers, emailAddress);
        }
    }
}
