package org.habilisoft.zemi.taxesmanagement.product.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "product_taxes")
public class ProductTax {
    @EmbeddedId
    @AttributeOverride(name = "productId.value", column = @Column(name = "product_id"))
    @AttributeOverride(name = "taxId.value", column = @Column(name = "tax_id"))
    private ProductTaxId id;

    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;

    public static ProductTax newTax(ProductId productId, TaxId taxId, LocalDateTime createdAt, Username createdBy) {
        ProductTax productTax = new ProductTax();
        productTax.id = new ProductTaxId(productId, taxId);
        productTax.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return productTax;
    }

    @Embeddable
    public record ProductTaxId(ProductId productId, TaxId taxId) implements Serializable { }
}
