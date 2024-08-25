package org.habilisoft.zemi.taxesmanagement.customer.domain;

import lombok.Getter;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.DomainException;
@Getter
public final class CustomerTaxNotFound extends DomainException {
    private final CustomerId customerId;
    public CustomerTaxNotFound(CustomerId customerId) {
        super("Customer %s tax not found".formatted(customerId.value()));
        this.customerId = customerId;
    }
}
