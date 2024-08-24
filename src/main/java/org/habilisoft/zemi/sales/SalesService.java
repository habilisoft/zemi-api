package org.habilisoft.zemi.sales;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.sales.customer.application.RegisterCustomer;
import org.habilisoft.zemi.sales.customer.application.RegisterCustomerUseCase;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final RegisterCustomerUseCase registerCustomerUseCase;

    public CustomerId registerCustomer(RegisterCustomer registerCustomer) {
        return registerCustomerUseCase.execute(registerCustomer);
    }

}
