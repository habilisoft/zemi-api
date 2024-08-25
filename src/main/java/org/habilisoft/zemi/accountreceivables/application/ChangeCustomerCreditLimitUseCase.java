package org.habilisoft.zemi.accountreceivables.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.accountreceivables.domain.CustomerAr;
import org.habilisoft.zemi.accountreceivables.domain.CustomerArNotFound;
import org.habilisoft.zemi.accountreceivables.domain.CustomerArRepository;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChangeCustomerCreditLimitUseCase implements UseCase<ChangeCustomerCreditLimit, Void> {
    private final CustomerArRepository customerArRepository;
    @Override
    public Void execute(ChangeCustomerCreditLimit changeCustomerCreditLimit) {
        CustomerId customerId = changeCustomerCreditLimit.customerId();
        CustomerAr customerAr = customerArRepository.findById(customerId)
                .orElseThrow(() -> new CustomerArNotFound(customerId));
        LocalDateTime updatedAt = changeCustomerCreditLimit.time();
        Username updatedBy = Username.of(changeCustomerCreditLimit.user());
        customerAr.changeCreditLimit(changeCustomerCreditLimit.newCreditLimit(), updatedAt, updatedBy);
        customerArRepository.save(customerAr);
        return null;
    }
}
