package org.habilisoft.zemi.taxesmanagement.customer.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.IdempotentUseCase;
import org.habilisoft.zemi.taxesmanagement.application.InitializeCustomerTax;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTax;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTaxRepository;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InitializeCustomerTaxUseCase implements IdempotentUseCase<InitializeCustomerTax, Void> {
    private final CustomerTaxRepository customerTaxRepository;
    @Override
    public String idempotencyKey(InitializeCustomerTax initializeCustomerTax) {
        return initializeCustomerTax.customerId().value().toString();
    }

    @Override
    public Void execute(InitializeCustomerTax initializeCustomerTax) {
        LocalDateTime createdAt = initializeCustomerTax.time();
        Username createdBy = Username.of(initializeCustomerTax.user());
        CustomerTax customerTax = CustomerTax.initialize(initializeCustomerTax.customerId(), createdAt, createdBy);
        customerTaxRepository.save(customerTax);
        return null;
    }
}
