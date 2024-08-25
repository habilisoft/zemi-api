package org.habilisoft.zemi.accountreceivables.domain;

import lombok.Getter;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.DomainException;
@Getter
public final class CustomerArNotFound extends DomainException {
    private final CustomerId customerId;
    public CustomerArNotFound(CustomerId customerId) {
        super("Customer %s don't have an account receivable record".formatted(customerId.value()));
        this.customerId = customerId;
    }
}
