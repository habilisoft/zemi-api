package org.habilisoft.zemi.taxesmanagement;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.sales.customer.domain.CustomerRegistered;
import org.habilisoft.zemi.taxesmanagement.application.ChangeCustomerNcfType;
import org.habilisoft.zemi.taxesmanagement.application.ChangeCustomerNcfTypeUseCase;
import org.habilisoft.zemi.taxesmanagement.application.InitializeCustomerTax;
import org.habilisoft.zemi.taxesmanagement.application.InitializeCustomerTaxUseCase;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxManagementService {
    private final InitializeCustomerTaxUseCase initializeCustomerTaxUseCase;
    private final ChangeCustomerNcfTypeUseCase changeCustomerNcfTypeUseCase;

    @ApplicationModuleListener
    void on(CustomerRegistered customerRegistered) {
        initializeCustomerTaxUseCase.execute(
                new InitializeCustomerTax(
                        customerRegistered.customerId(),
                        customerRegistered.time(),
                        customerRegistered.user())
        );
    }

    public void changeCustomerNcfType(ChangeCustomerNcfType changeCustomerNcfType) {
        changeCustomerNcfTypeUseCase.execute(changeCustomerNcfType);
    }
}
