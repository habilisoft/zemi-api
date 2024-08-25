package org.habilisoft.zemi.sales.sale.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRate;

import java.io.Serializable;

@Data
@Entity
@Table(name = "sale_product_taxes")
@EqualsAndHashCode(of = "id")
public class SaleProductTax {
    @EmbeddedId
    @AttributeOverride(name = "transactionalId.documentId.value", column = @Column(name = "sale_document"))
    @AttributeOverride(name = "transactionalId.sequence", column = @Column(name = "sale_sequence"))
    @AttributeOverride(name = "taxId.value", column = @Column(name = "tax_id"))
    @AttributeOverride(name = "productId.value", column = @Column(name = "product_id"))
    private SaleProductTaxId id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tax_rate"))
    private TaxRate taxRate;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tax_amount"))
    private MonetaryAmount taxAmount;
    @Embeddable
    public record SaleProductTaxId(TransactionalId transactionalId, TaxId taxId, ProductId productId) implements Serializable { }

    static SaleProductTax of(TransactionalId saleId, ProductId productId, TaxId taxId, TaxRate taxRate, MonetaryAmount price) {
        SaleProductTax saleProductTax = new SaleProductTax();
        saleProductTax.id = new SaleProductTaxId(saleId, taxId, productId);
        saleProductTax.taxRate = taxRate;
        saleProductTax.taxAmount = taxRate.calculateTax(price);
        return saleProductTax;
    }

    public TaxId taxId() {
        return id.taxId;
    }
}
