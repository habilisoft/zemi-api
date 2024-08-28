package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "price_list_product")
public class PriceListProduct implements Persistable<PriceListProductId> {
    @EmbeddedId
    @AttributeOverride(name = "priceListId.value", column = @Column(name = "price_list_id"))
    @AttributeOverride(name = "productId.value", column = @Column(name = "product_id"))
    @AttributeOverride(name = "date", column = @Column(name = "date"))
    private PriceListProductId id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private MonetaryAmount price;
    private boolean isCurrent;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Transient
    private boolean isNew;

    public static PriceListProduct newPrice(PriceListId priceListId, ProductId productId, MonetaryAmount price, LocalDateTime createdAt, Username createdBy) {
        PriceListProduct priceListProduct = new PriceListProduct();
        priceListProduct.id = new PriceListProductId(priceListId, productId, createdAt);
        priceListProduct.price = price;
        priceListProduct.isCurrent = true;
        priceListProduct.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        priceListProduct.isNew = true;
        return priceListProduct;
    }

    public void deactivate(LocalDateTime updatedAt, Username updatedBy) {
        this.isCurrent = false;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }

    public ProductId getProductId() {
        return id.productId();
    }
}
