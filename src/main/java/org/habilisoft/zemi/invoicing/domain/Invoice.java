package org.habilisoft.zemi.invoicing.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.Ncf;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "invoices")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Invoice implements Persistable<TransactionalId> {
    @EmbeddedId
    @AttributeOverride(name = "sequence", column = @Column(name = "sequence"))
    @AttributeOverride(name = "documentId.value", column = @Column(name = "document"))
    private TransactionalId id;
    @AttributeOverride(name = "value", column = @Column(name = "customer_id"))
    private CustomerId customerId;
    @AttributeOverride(name = "value", column = @Column(name = "ncf"))
    private Ncf ncf;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "total"))
    private MonetaryAmount total;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "balance"))
    private MonetaryAmount balance;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Version
    private Long version;
    @Transient
    private boolean isNew;


    public static Invoice generate(TransactionalId id, CustomerId customerId, Ncf ncf, MonetaryAmount total, LocalDateTime createdAt, Username createdBy) {
        Invoice invoice = new Invoice();
        invoice.id = id;
        invoice.ncf = ncf;
        invoice.customerId = customerId;
        invoice.total = total;
        invoice.balance = total;
        invoice.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return invoice;
    }
}
