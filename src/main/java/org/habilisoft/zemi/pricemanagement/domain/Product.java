package org.habilisoft.zemi.pricemanagement.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Data
@Table(name = "products")
@Entity(name = "Product_Price_Management")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Product extends AbstractAggregateRoot<Product> {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ProductId id;
    @OneToMany(mappedBy = "id.productId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProductPrice> prices = Set.of();

    public void changePrice(MonetaryAmount price, LocalDateTime createdAt, Username createdBy) {
        ProductPrice newPrice = ProductPrice.newPrice(id, price, createdAt, createdBy);
        Optional<ProductPrice> oldPrice = prices.stream().filter(ProductPrice::isCurrent).findFirst();
        oldPrice.ifPresent(productPrice -> productPrice.deactivate(createdAt, createdBy));
        prices.add(newPrice);
        registerEvent(new ProductPriceChanged(id, oldPrice.map(ProductPrice::getPrice), price));
    }
}
