package org.habilisoft.zemi.accountreceivables;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.accountreceivables.application.ChangeCustomerCreditLimit;
import org.habilisoft.zemi.accountreceivables.application.ChangeCustomerCreditLimitUseCase;
import org.habilisoft.zemi.accountreceivables.application.InitializeCustomerAr;
import org.habilisoft.zemi.accountreceivables.application.InitializeCustomerArUseCase;
import org.habilisoft.zemi.sales.customer.domain.CustomerRegistered;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountReceivablesService {
    private final InitializeCustomerArUseCase initializeCustomerArUseCase;
    private final ChangeCustomerCreditLimitUseCase changeCustomerCreditLimitUseCase;

    @ApplicationModuleListener
    void on(CustomerRegistered customerRegistered) {
        initializeCustomerArUseCase.execute(
                new InitializeCustomerAr(
                        customerRegistered.customerId(),
                        customerRegistered.time(),
                        customerRegistered.user()
                )
        );
    }

    public void changeCreditLimit(ChangeCustomerCreditLimit changeCustomerCreditLimit) {
        changeCustomerCreditLimitUseCase.execute(changeCustomerCreditLimit);
    }
}
