package org.habilisoft.zemi.customer.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.customer.CustomerService;
import org.habilisoft.zemi.customer.application.RegisterCustomer;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("customers/v1")
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registerCustomer(@RequestBody @Valid Requests.RegisterCustomer registerCustomer) {
        CustomerId customerId = customerService.registerCustomer(
                new RegisterCustomer(
                        registerCustomer.name(),
                        registerCustomer.type(),
                        registerCustomer.contact(),
                        registerCustomer.address(),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
        return Map.of("id", customerId);
    }
}
