package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "price_lists")
@EqualsAndHashCode(of = "id", callSuper = false)
public class PriceList extends AbstractAggregateRoot<PriceList> implements Persistable<PriceListId> {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private PriceListId id;
    private String name;
    @OneToMany(mappedBy = "id.priceListId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PriceListProduct> products;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Transient
    private boolean isNew;

    public static PriceList newPriceList(PriceListId priceListId, String name, Set<ProductIdAndPrice> products, LocalDateTime createdAt, Username createdBy) {
        PriceList priceList = new PriceList();
        priceList.id = priceListId;
        priceList.name = name;
        priceList.products = products.stream().map(product -> PriceListProduct.newPrice(priceListId, product.productId(), product.price(), createdAt, createdBy))
                .collect(Collectors.toSet());
        priceList.isNew = true;
        priceList.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return priceList;
    }

    public PriceList clone(PriceListId priceListId, String name, LocalDateTime createdAt, Username createdBy) {
        Set<ProductIdAndPrice> productsToClone = this.products.stream()
                .filter(product -> Objects.nonNull(product.getId()))
                .filter(PriceListProduct::isCurrent)
                .map(product -> new ProductIdAndPrice(product.getId().productId(), product.getPrice()))
                .collect(Collectors.toSet());
        return PriceList.newPriceList(priceListId, name, productsToClone, createdAt, createdBy);
    }

    public void changeProductPrice(Set<ProductIdAndPrice> productIdAndPrices, LocalDateTime updatedAt, Username updatedBy) {
        Map<ProductId, MonetaryAmount> newPriceMap = productIdAndPrices.stream()
                .collect(Collectors.toMap(ProductIdAndPrice::productId, ProductIdAndPrice::price));
        Map<ProductId, PriceListProduct> currentPriceMap = currentProductPriceMap();

        newPriceMap.forEach((productId, price) -> {
            currentPriceMap.computeIfPresent(productId, (_, product) -> {
                product.deactivate(updatedAt, updatedBy);
                return product;
            });
            products.add(PriceListProduct.newPrice(id, productId, price, updatedAt, updatedBy));
        });
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }

    public void addProducts(Set<ProductIdAndPrice> productIdAndPrice, LocalDateTime updatedAt, Username updatedBy) {
        Map<ProductId, PriceListProduct> currentPriceMap = currentProductPriceMap();
        Set<PriceListProduct> newPriceListProducts = productIdAndPrice.stream()
                .filter(product -> !currentPriceMap.containsKey(product.productId()))
                .map(product -> PriceListProduct.newPrice(id, product.productId(), product.price(), updatedAt, updatedBy))
                .collect(Collectors.toSet());
        this.products.addAll(newPriceListProducts);
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }

    private Map<ProductId, PriceListProduct> currentProductPriceMap() {
        return this.products.stream()
                .filter(product -> Objects.nonNull(product.getId()))
                .filter(PriceListProduct::isCurrent)
                .collect(Collectors.toMap(product -> product.getId().productId(), Function.identity()));
    }

    public void removeProduct(Set<ProductId> productIds, LocalDateTime updatedAt, Username updatedBy) {
        this.products.stream()
                .filter(product -> Objects.nonNull(product.getId()))
                .filter(product -> productIds.contains(product.getId().productId()))
                .forEach(product -> product.deactivate(updatedAt, updatedBy));
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }

    public void update(String name, LocalDateTime updatedAt, Username updatedBy) {
        this.name = name;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
}
