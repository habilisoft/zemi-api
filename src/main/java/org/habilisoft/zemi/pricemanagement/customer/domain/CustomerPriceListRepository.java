package org.habilisoft.zemi.pricemanagement.customer.domain;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerPriceListRepository extends JpaRepository<CustomerPriceList, CustomerId> {
    @Query("""
            select pp from CustomerPriceList cpl, PriceList pl, PriceListProduct pp
            where cpl.id = :customerId and cpl.priceListId = pl.id and pl.id = pp.id.priceListId and pp.id.productId in :productIds and pp.isCurrent = true
            """)
    List<PriceListProduct> findCurrentPriceByProductIds(CustomerId customerId, Iterable<ProductId> productIds);
}
