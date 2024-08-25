package org.habilisoft.zemi.taxesmanagement.product.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Table(name = "products")
@Entity(name = "Product_Tax_Management")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Product extends AbstractAggregateRoot<Product> {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ProductId id;
    @OneToMany(mappedBy = "id.productId", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProductTax> taxes;

    public void addTaxes(Set<TaxId> taxIds, LocalDateTime createdAt, Username createdBy) {
        taxIds.stream().filter(taxId -> !taxes.contains(taxId))
                .map(taxId -> ProductTax.newTax(id, taxId, createdAt, createdBy))
                .forEach(taxes::add);
    }
    public void removeTax(Set<TaxId> taxIds, LocalDateTime updatedAt, Username updatedBy) {
        taxes.removeIf(productTax -> taxIds.contains(productTax.getId().taxId()));
    }
}
