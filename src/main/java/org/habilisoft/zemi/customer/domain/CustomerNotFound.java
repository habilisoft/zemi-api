package org.habilisoft.zemi.customer.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
import org.springframework.modulith.NamedInterface;

@Getter
@NamedInterface
public final class CustomerNotFound extends DomainException {
    private final CustomerId customerId;

    public CustomerNotFound(CustomerId customerId) {
        super("Customer %s not found".formatted(customerId.value()));
        this.customerId = customerId;
    }
}
