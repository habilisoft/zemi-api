package org.habilisoft.zemi.sales.sale.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "sales")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Sale extends AbstractAggregateRoot<Sale> implements Persistable<TransactionalId> {
    @EmbeddedId
    @AttributeOverride(name = "sequence", column = @Column(name = "sequence"))
    @AttributeOverride(name = "documentId.value", column = @Column(name = "document"))
    private TransactionalId id;
    private LocalDateTime date;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "total"))
    private MonetaryAmount total;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "customer_id"))
    private CustomerId customerId;
    @OneToMany(mappedBy = "id.transactionalId", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<SaleProduct> products;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Transient
    private boolean isNew;

    public static Sale makeSaleForCustomer(TransactionalId id, CustomerId customerId, Set<SaleProduct> products, LocalDateTime createdAt, Username createdBy) {
        Sale sale = makeSale(id, products, createdAt, createdBy);
        sale.customerId = customerId;
        return sale;
    }
    public static Sale makeSale(TransactionalId id, Set<SaleProduct> products, LocalDateTime createdAt, Username createdBy) {
        Sale sale = new Sale();
        sale.id = id;
        sale.date = LocalDateTime.now();
        sale.total = products.stream().map(SaleProduct::total).reduce(MonetaryAmount.ZERO, MonetaryAmount::add);
        sale.products = products;
        sale.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        sale.isNew = true;
        return sale;
    }
}
