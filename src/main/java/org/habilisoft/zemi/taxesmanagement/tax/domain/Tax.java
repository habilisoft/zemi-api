package org.habilisoft.zemi.taxesmanagement.tax.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "taxes")
public class Tax extends AbstractAggregateRoot<Tax> implements Persistable<TaxId> {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private TaxId id;
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "rate"))
    private TaxRate rate;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Transient
    private boolean isNew;

    public static Tax newTax(TaxId taxId, String name, TaxRate rate, LocalDateTime createdAt, Username createdBy) {
        Tax tax = new Tax();
        tax.id = taxId;
        tax.name = name;
        tax.rate = rate;
        tax.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        tax.isNew = true;
        return tax;
    }

    public void update(String name, TaxRate rate, LocalDateTime updatedAt, Username updatedBy) {
        this.name = name;
        this.rate = rate;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
}
