package org.habilisoft.zemi.pricemanagement;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.pricemanagement.pricelist.application.*;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListProduct;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListRepository;
import org.habilisoft.zemi.pricemanagement.product.application.ChangeProductPrice;
import org.habilisoft.zemi.pricemanagement.product.application.ChangeProductPriceUseCase;
import org.habilisoft.zemi.pricemanagement.product.domain.ProductPrice;
import org.habilisoft.zemi.pricemanagement.product.domain.ProductPriceRepository;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class PriceManagementService {
    private final ChangeProductPriceUseCase changeProductPriceUseCase;
    private final ProductPriceRepository productPriceRepository;
    private final RemoveProductsToPriceListUseCase removeProductsToPriceListUseCase;
    private final AddProductsToPriceListUseCase addProductsToPriceListUseCase;
    private final CreatePriceListUseCase createPriceListUseCase;
    private final ClonePriceListUseCase clonePriceListUseCase;
    private final ChangeProductPriceCurrentPriceUseCase changeProductPriceCurrentPriceUseCase;
    private final PriceListRepository priceListRepository;

    public void changeProductPrice(ChangeProductPrice changeProductPrice) {
        changeProductPriceUseCase.execute(changeProductPrice);
    }

    public Map<ProductId, MonetaryAmount> getCurrentPrice(Set<ProductId> productIds) {
        return productPriceRepository.findCurrentPriceByProductIds(productIds)
                .stream().collect(toMap(productPrice -> productPrice.getId().productId(), ProductPrice::getPrice));
    }

    public void removeProductsToPriceList(RemoveProductsToPriceList removeProductsToPriceList) {
        removeProductsToPriceListUseCase.execute(removeProductsToPriceList);
    }

    public void addProductsToPriceList(AddProductsToPriceList addProductsToPriceList) {
        addProductsToPriceListUseCase.execute(addProductsToPriceList);
    }

    public PriceListId createPriceList(CreatePriceList createPriceList) {
        return createPriceListUseCase.execute(createPriceList);
    }

    public PriceListId clonePriceList(ClonePriceList clonePriceList) {
        return clonePriceListUseCase.execute(clonePriceList);
    }

    public void changeProductPriceCurrentPrice(ChangeProductPriceCurrentPrice changeProductPriceCurrentPrice) {
        changeProductPriceCurrentPriceUseCase.execute(changeProductPriceCurrentPrice);
    }

    public Map<ProductId, MonetaryAmount> getCurrentPrice(PriceListId priceListId, Set<ProductId> productIds) {
        return priceListRepository.findCurrentPriceByProductIds(priceListId, productIds).stream()
                .collect(toMap(PriceListProduct::getProductId, PriceListProduct::getPrice));
    }
}
