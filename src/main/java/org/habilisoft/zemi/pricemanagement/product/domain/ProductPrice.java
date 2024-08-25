package org.habilisoft.zemi.pricemanagement.product.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "product_prices")
public class ProductPrice {
    @EmbeddedId
    @AttributeOverride(name = "productId.value", column = @Column(name = "product_id"))
    @AttributeOverride(name = "date", column = @Column(name = "date"))
    private ProductPriceId id;
    private boolean isCurrent;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private MonetaryAmount price;

    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;

    public static ProductPrice newPrice(ProductId productId, MonetaryAmount price, LocalDateTime createdAt, Username createdBy) {
        ProductPrice productPrice = new ProductPrice();
        productPrice.id = new ProductPriceId(productId, createdAt);
        productPrice.price = price;
        productPrice.isCurrent = true;
        productPrice.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return productPrice;
    }

    public void deactivate(LocalDateTime updatedAt, Username updatedBy) {
        this.isCurrent = false;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }

    @Embeddable
    public record ProductPriceId(ProductId productId, LocalDateTime date) implements Serializable { }
}
