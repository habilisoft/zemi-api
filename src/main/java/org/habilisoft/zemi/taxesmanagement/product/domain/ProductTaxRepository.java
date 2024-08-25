package org.habilisoft.zemi.taxesmanagement.product.domain;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ProductTaxRepository extends JpaRepository<Product, ProductId> {
    @Query("""
            select p.id.productId as productId, t as tax
            from ProductTax p, Tax t
            where t.id = p.id.taxId and p.id.productId in :productIds
            """)
    Set<ProductIdAndTax> findTaxesByProductIds(Set<ProductId> productIds);

}
