package org.habilisoft.zemi.sales.sale.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.Quantity;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.taxesmanagement.product.domain.TaxIdAndRate;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sale_document", referencedColumnName = "sale_document")
    @JoinColumn(name = "sale_sequence", referencedColumnName = "sale_sequence")
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    Set<SaleProductTax> taxes;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tax_amount"))
    private MonetaryAmount taxAmount;

    public record SaleProductId(TransactionalId transactionalId, ProductId productId) implements Serializable {
        static SaleProductId of(TransactionalId transactionalId, ProductId productId) {
            return new SaleProductId(transactionalId, productId);
        }
    }

    public static SaleProduct of(TransactionalId saleId, ProductId productId, Quantity quantity, MonetaryAmount price, Set<TaxIdAndRate> taxRates) {
        SaleProduct saleItem = new SaleProduct();
        saleItem.id = SaleProductId.of(saleId, productId);
        saleItem.quantity = quantity;
        saleItem.price = price;
        Set<SaleProductTax> taxes = taxRates.stream().map(taxRate -> SaleProductTax.of(saleId, productId, taxRate.taxId(), taxRate.taxRate(), price))
                .collect(Collectors.toSet());
        MonetaryAmount taxAmount = taxes.stream().map(SaleProductTax::getTaxAmount).reduce(MonetaryAmount.ZERO, MonetaryAmount::add);
        saleItem.taxes = taxes;
        saleItem.taxAmount = taxAmount;
        return saleItem;
    }

    public MonetaryAmount total() {
        return price.add(taxAmount).multiply(quantity);
    }

    public ProductId productId() {
        return id.productId();
    }
}
