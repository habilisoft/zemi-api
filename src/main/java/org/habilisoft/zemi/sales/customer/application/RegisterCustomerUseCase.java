package org.habilisoft.zemi.sales.customer.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.sales.customer.domain.Customer;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.sales.customer.domain.CustomerIdGenerator;
import org.habilisoft.zemi.sales.customer.domain.CustomerRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterCustomerUseCase implements UseCase<RegisterCustomer, CustomerId> {
    private final CustomerIdGenerator customerIdGenerator;
    private final CustomerRepository customerRepository;
    @Override
    public CustomerId execute(RegisterCustomer registerCustomer) {
        LocalDateTime createdAt = registerCustomer.time();
        Username createdBy = Username.of(registerCustomer.user());
        CustomerId customerId = customerIdGenerator.generate();
        Customer customer = Customer.register(
                customerId,
                registerCustomer.name(),
                registerCustomer.type(),
                registerCustomer.address(),
                registerCustomer.contact(),
                createdAt,
                createdBy
        );
        registerCustomer.creditLimit().ifPresent(customer::changeCreditLimit);
        registerCustomer.ncfType().ifPresent(customer::changeNcfType);
        customerRepository.save(customer);
        return customerId;
    }
}
