package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PriceListRepository extends JpaRepository<PriceList, PriceListId> {
    @Query("from PriceListProduct pp where pp.id.priceListId = :priceListId and pp.id.productId in :productIds and pp.isCurrent = true")
    Set<PriceListProduct> findCurrentPriceByProductIds(PriceListId priceListId, Iterable<ProductId> productIds);
}
