package org.habilisoft.zemi.pricemanagement.product.domain;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductPriceRepository extends JpaRepository<Product, ProductId> {
    @Query("from ProductPrice pp where pp.isCurrent = true and pp.id.productId in :productIds")
    List<ProductPrice> findCurrentPriceByProductIds(Iterable<ProductId> productIds);
}
