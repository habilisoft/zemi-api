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
public class CreatePriceListUseCase implements UseCase<CreatePriceList, PriceListId> {
    private final PriceListIdGenerator priceListIdGenerator;
    private final PriceListRepository priceListRepository;
    private final CatalogService catalogService;

    @Override
    public PriceListId execute(CreatePriceList createPriceList) {
        Set<ProductId> productIds = createPriceList.products().stream()
                .map(ProductIdAndPrice::productId).collect(Collectors.toSet());
        Map<ProductId, Boolean> existenceMap = catalogService.productExists(productIds);
        existenceMap.entrySet().stream().filter(Predicate.not(Map.Entry::getValue)).findAny().ifPresent(entry -> {
            throw new ProductNotFound(entry.getKey());
        });
        PriceListId priceListId = priceListIdGenerator.generate();
        LocalDateTime time = createPriceList.time();
        Username user = Username.of(createPriceList.user());
        PriceList priceList = PriceList.newPriceList(priceListId, createPriceList.name(), createPriceList.products(), time, user);
        priceListRepository.save(priceList);
        return priceListId;
    }
}
