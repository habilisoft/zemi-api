package org.habilisoft.zemi.pricemanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.pricemanagement.PriceManagementService;
import org.habilisoft.zemi.pricemanagement.application.ChangeProductPrice;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/price-management/v1")
class PriceManagementController {
    private final PriceManagementService priceManagementService;
    private final UserService userService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/product/{productId}/change-price")
    void changeProductPrice(@PathVariable Long productId, @RequestBody @Valid Requests.ChangeProductPrice changeProductPrice) {
        priceManagementService.changeProductPrice(
                new ChangeProductPrice(
                        ProductId.of(productId),
                        MonetaryAmount.of(changeProductPrice.price()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
    }
}
