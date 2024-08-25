package org.habilisoft.zemi.sales.sale.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.Quantity;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRate;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "sale_products")
@EqualsAndHashCode(of = "id", callSuper = false)
public class SaleProduct {
    @EmbeddedId
    @AttributeOverride(name = "transactionalId.documentId.value", column = @Column(name = "sale_document"))
    @AttributeOverride(name = "transactionalId.sequence", column = @Column(name = "sale_sequence"))
    @AttributeOverride(name = "productId.value", column = @Column(name = "product_id"))
    private SaleProductId id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    private Quantity quantity;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private MonetaryAmount price;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tax_amount"))
    private MonetaryAmount taxAmount;
    record SaleProductId(TransactionalId transactionalId, ProductId productId) implements Serializable {
        static SaleProductId of(TransactionalId transactionalId, ProductId productId) {
            return new SaleProductId(transactionalId, productId);
        }
    }
    public static SaleProduct of(TransactionalId saleId, ProductId productId, Quantity quantity, MonetaryAmount price, Set<TaxRate> taxRates) {
        SaleProduct saleItem = new SaleProduct();
        saleItem.id = SaleProductId.of(saleId, productId);
        saleItem.quantity = quantity;
        saleItem.price = price;
        MonetaryAmount taxAmount = taxRates.stream()
                .map(taxRate -> taxRate.calculateTax(price))
                .reduce(MonetaryAmount.ZERO, MonetaryAmount::add);
        saleItem.taxAmount = taxAmount;
        return saleItem;
    }

    public MonetaryAmount total() {
        return price.multiply(quantity);
    }
}
