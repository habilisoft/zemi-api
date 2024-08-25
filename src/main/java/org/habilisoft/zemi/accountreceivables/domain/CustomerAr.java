package org.habilisoft.zemi.accountreceivables.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_ar_settings")
@EqualsAndHashCode(of = "", callSuper = false)
public class CustomerAr extends AbstractAggregateRoot<CustomerAr> implements Persistable<CustomerId> {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "customer_id"))
    private CustomerId id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "credit_limit"))
    private MonetaryAmount creditLimit;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Transient
    private boolean isNew;

    public static CustomerAr initialize(CustomerId customerId, LocalDateTime createdAt, Username createdBy) {
        CustomerAr settings = new CustomerAr();
        settings.id = customerId;
        settings.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        settings.isNew = true;
        return settings;
    }

    public void changeCreditLimit(MonetaryAmount newCreditLimit, LocalDateTime updatedAt, Username updatedBy) {
        this.creditLimit = newCreditLimit;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
        this.registerEvent(new CustomerCreditLimitChanged(this.id, newCreditLimit));
    }
}
