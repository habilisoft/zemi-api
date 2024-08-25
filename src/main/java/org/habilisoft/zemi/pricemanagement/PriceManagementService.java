package org.habilisoft.zemi.pricemanagement;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.pricemanagement.application.ChangeProductPrice;
import org.habilisoft.zemi.pricemanagement.application.ChangeProductPriceUseCase;
import org.habilisoft.zemi.pricemanagement.domain.ProductPrice;
import org.habilisoft.zemi.pricemanagement.domain.ProductPriceRepository;
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

    public void changeProductPrice(ChangeProductPrice changeProductPrice) {
        changeProductPriceUseCase.execute(changeProductPrice);
    }

    public Map<ProductId, MonetaryAmount> getCurrentPrice(Set<ProductId> productIds) {
        return productPriceRepository.findCurrentPriceByProductIds(productIds)
                .stream().collect(toMap(productPrice -> productPrice.getId().productId(), ProductPrice::getPrice));
    }
}
