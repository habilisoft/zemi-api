package org.habilisoft.zemi.taxesmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.taxesmanagement.TaxManagementService;
import org.habilisoft.zemi.taxesmanagement.application.ChangeCustomerNcfType;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("tax-management/v1")
class TaxManagementController {
    private final TaxManagementService taxManagementService;
    private final UserService userService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/customer/{customerId}/change-ncf-type")
    void changeNcfType(@PathVariable Long customerId, @RequestBody @Valid Requests.ChangeNcfType changeNcfType) {
        taxManagementService.changeCustomerNcfType(
                new ChangeCustomerNcfType(
                        CustomerId.of(customerId),
                        changeNcfType.ncfType(),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
    }
}
