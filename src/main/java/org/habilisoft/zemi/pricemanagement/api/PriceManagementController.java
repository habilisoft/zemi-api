package org.habilisoft.zemi.pricemanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.pricemanagement.PriceManagementService;
import org.habilisoft.zemi.pricemanagement.pricelist.application.ChangeProductPriceCurrentPrice;
import org.habilisoft.zemi.pricemanagement.pricelist.application.ClonePriceList;
import org.habilisoft.zemi.pricemanagement.pricelist.application.CreatePriceList;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.pricemanagement.product.application.ChangeProductPrice;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/price-list")
    Map<String, Object> createPriceList(@RequestBody @Valid Requests.CreatePriceList createPriceList) {
        PriceListId priceList = priceManagementService.createPriceList(
                new CreatePriceList(
                        createPriceList.name(),
                        createPriceList.products().stream().map(Requests.ProductAndPrice::toProductIdAndPrice).collect(Collectors.toSet()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
        return Map.of("id", priceList.value());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/price-list/{priceListId}/clone")
    Map<String, Object> clonePriceList(@PathVariable Long priceListId, @RequestBody @Valid Requests.ClonePriceList clonePriceList) {
        PriceListId priceList = priceManagementService.clonePriceList(
                new ClonePriceList(
                        PriceListId.of(priceListId),
                        clonePriceList.name(),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
        return Map.of("id", priceList.value());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/price-list/{priceListId}/add-products")
    void addProductsToPriceList(@PathVariable Long priceListId, @RequestBody @Valid Requests.AddProductsToPriceList addProductsToPriceList) {
        priceManagementService.addProductsToPriceList(
                new org.habilisoft.zemi.pricemanagement.pricelist.application.AddProductsToPriceList(
                        PriceListId.of(priceListId),
                        addProductsToPriceList.products().stream().map(Requests.ProductAndPrice::toProductIdAndPrice).collect(Collectors.toSet()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/price-list/{priceListId}/remove-products")
    void removeProductsToPriceList(@PathVariable Long priceListId, @RequestBody @Valid Requests.RemoveProductsToPriceList removeProductsToPriceList) {
        priceManagementService.removeProductsToPriceList(
                new org.habilisoft.zemi.pricemanagement.pricelist.application.RemoveProductsToPriceList(
                        PriceListId.of(priceListId),
                        removeProductsToPriceList.products().stream().map(ProductId::of).collect(Collectors.toSet()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                )
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/price-list/{priceListId}/change-products-price")
    void changeProductPriceCurrentPrice(@PathVariable Long priceListId, @RequestBody @Valid Requests.ChangeProductPriceCurrentPrice changeProductPriceCurrentPrice) {
        priceManagementService.changeProductPriceCurrentPrice(
                new ChangeProductPriceCurrentPrice(
                        PriceListId.of(priceListId),
                        changeProductPriceCurrentPrice.products().stream().map(Requests.ProductAndPrice::toProductIdAndPrice).collect(Collectors.toSet()),
                        LocalDateTime.now(),
                        userService.getCurrentUser()
                ));
    }
}
