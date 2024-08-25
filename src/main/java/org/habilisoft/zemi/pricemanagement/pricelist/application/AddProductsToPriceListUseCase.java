package org.habilisoft.zemi.pricemanagement.pricelist.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.CatalogService;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductNotFound;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.*;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddProductsToPriceListUseCase implements UseCase<AddProductsToPriceList, Void> {
    private final PriceListRepository priceListRepository;
    private final CatalogService catalogService;

    @Override
    public Void execute(AddProductsToPriceList addProductsToPriceList) {
        PriceListId priceListId = addProductsToPriceList.priceListId();
        PriceList priceList = priceListRepository.findById(priceListId)
                .orElseThrow(() -> new PriceListNotFound(priceListId));
        Set<ProductId> productIds = addProductsToPriceList.prices().stream()
                .map(ProductIdAndPrice::productId).collect(Collectors.toSet());
        Map<ProductId, Boolean> existenceMap = catalogService.productExists(productIds);
        existenceMap.entrySet().stream().filter(Predicate.not(Map.Entry::getValue)).findAny().ifPresent(entry -> {
            throw new ProductNotFound(entry.getKey());
        });
        Username username = Username.of(addProductsToPriceList.user());
        LocalDateTime time = addProductsToPriceList.time();
        priceList.addProducts(addProductsToPriceList.prices(), time, username);
        priceListRepository.save(priceList);
        return null;
    }
}
