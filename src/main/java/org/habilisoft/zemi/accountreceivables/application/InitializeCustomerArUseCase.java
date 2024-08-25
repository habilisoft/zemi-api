package org.habilisoft.zemi.accountreceivables.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.accountreceivables.domain.CustomerAr;
import org.habilisoft.zemi.accountreceivables.domain.CustomerArRepository;
import org.habilisoft.zemi.shared.IdempotentUseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InitializeCustomerArUseCase implements IdempotentUseCase<InitializeCustomerAr, Void> {
    private final CustomerArRepository customerArRepository;
    @Override
    public Void execute(InitializeCustomerAr initializeCustomerAr) {
        LocalDateTime createdAt = initializeCustomerAr.time();
        Username createdBy = Username.of(initializeCustomerAr.user());
        CustomerAr customerAr = CustomerAr.initialize(initializeCustomerAr.customerId(), createdAt, createdBy);
        customerArRepository.save(customerAr);
        return null;
    }

    @Override
    public String idempotencyKey(InitializeCustomerAr initializeCustomerAr) {
        return initializeCustomerAr.customerId().value().toString();
    }
}
