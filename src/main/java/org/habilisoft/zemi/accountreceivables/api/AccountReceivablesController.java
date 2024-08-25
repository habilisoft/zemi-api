package org.habilisoft.zemi.accountreceivables.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.accountreceivables.AccountReceivablesService;
import org.habilisoft.zemi.accountreceivables.application.ChangeCustomerCreditLimit;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("account-receivables/v1")
class AccountReceivablesController {
    private final AccountReceivablesService accountReceivablesService;
    private final UserService userService;

    @PostMapping("/customer/{customerId}/change-credit-limit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changeCreditLimit(@PathVariable Long customerId, @RequestBody @Valid Requests.ChangeCreditLimit changeCreditLimit) {
        accountReceivablesService.changeCreditLimit(
                new ChangeCustomerCreditLimit(
                        CustomerId.of(customerId),
                        MonetaryAmount.of(changeCreditLimit.creditLimit()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
    }
}
