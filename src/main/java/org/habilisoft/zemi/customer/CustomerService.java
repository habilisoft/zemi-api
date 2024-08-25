package org.habilisoft.zemi.customer;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.customer.application.RegisterCustomer;
import org.habilisoft.zemi.customer.application.RegisterCustomerUseCase;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.customer.domain.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final CustomerRepository customerRepository;

    public CustomerId registerCustomer(RegisterCustomer registerCustomer) {
        return registerCustomerUseCase.execute(registerCustomer);
    }

    public boolean exists(CustomerId customerId) {
        return customerRepository.existsById(customerId);
    }
}
