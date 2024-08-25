package org.habilisoft.zemi.taxesmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.taxesmanagement.TaxManagementService;
import org.habilisoft.zemi.taxesmanagement.application.ChangeCustomerNcfType;
import org.habilisoft.zemi.taxesmanagement.product.application.AddProductTaxes;
import org.habilisoft.zemi.taxesmanagement.product.application.RemoveProductTaxes;
import org.habilisoft.zemi.taxesmanagement.tax.application.CreateTax;
import org.habilisoft.zemi.taxesmanagement.tax.application.UpdateTax;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRate;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tax")
    Map<String, Object> createTax(@RequestBody @Valid Requests.CreateTax createTax) {
        TaxId taxId = taxManagementService.createTax(
                new CreateTax(
                        createTax.name(),
                        TaxRate.fromPercentage(createTax.rate()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                ));
        return Map.of("id", taxId.value());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/tax/{taxId}")
    void updateTax(@PathVariable Long taxId, @RequestBody @Valid Requests.UpdateTax updateTax) {
        taxManagementService.updateTax(
                new UpdateTax(
                        TaxId.of(taxId),
                        updateTax.name(),
                        TaxRate.fromPercentage(updateTax.rate()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                ));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/product/{productId}/tax")
    void addProductTax(@PathVariable Long productId, @RequestBody @Valid Requests.AddProductTax addProductTax) {
        taxManagementService.addProductTaxes(
                new AddProductTaxes(
                        ProductId.of(productId),
                        addProductTax.taxes(),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/product/{productId}/tax/remove")
    void removeProductTax(@PathVariable Long productId, @RequestBody @Valid Requests.RemoveProductTax removeProductTax) {
        taxManagementService.removeProductTaxes(
                new RemoveProductTaxes(
                        ProductId.of(productId),
                        removeProductTax.taxes(),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
    }
}
