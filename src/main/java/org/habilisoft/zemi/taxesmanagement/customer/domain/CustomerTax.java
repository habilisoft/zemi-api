package org.habilisoft.zemi.taxesmanagement.customer.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.taxesmanagement.ncf.NcfType;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_tax_settings")
@EqualsAndHashCode(callSuper = false, of = "id")
public class CustomerTax extends AbstractAggregateRoot<CustomerTax> implements Persistable<CustomerId> {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "customer_id"))
    private CustomerId id;
    @Enumerated(EnumType.STRING)
    private NcfType ncfType;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Transient
    private boolean isNew;

    public static CustomerTax initialize(CustomerId customerId, LocalDateTime createdAt, Username createdBy) {
        CustomerTax customerTax = new CustomerTax();
        customerTax.id = customerId;
        customerTax.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        customerTax.isNew = true;
        return customerTax;
    }

    public void changeNcfType(NcfType ncfType, LocalDateTime updatedAt, Username updatedBy) {
        this.ncfType = ncfType;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
        registerEvent(new CustomerNcfTypeChanged(id, ncfType));
    }
}
