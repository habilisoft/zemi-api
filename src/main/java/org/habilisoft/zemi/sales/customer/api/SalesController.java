package org.habilisoft.zemi.sales.customer.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.sales.SalesService;
import org.habilisoft.zemi.sales.customer.application.RegisterCustomer;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("sales/v1")
public class SalesController {
    private final SalesService salesService;
    private final UserService userService;

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registerCustomer(@RequestBody @Valid Requests.RegisterCustomer registerCustomer) {
        CustomerId customerId = salesService.registerCustomer(
                new RegisterCustomer(
                        registerCustomer.name(),
                        registerCustomer.type(),
                        registerCustomer.contact(),
                        registerCustomer.address(),
                        registerCustomer.ncfType(),
                        registerCustomer.creditLimit(),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
        return Map.of("id", customerId);
    }
}
