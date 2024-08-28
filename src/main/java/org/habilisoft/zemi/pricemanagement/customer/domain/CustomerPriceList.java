package org.habilisoft.zemi.pricemanagement.customer.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "customer_price_lists")
public class CustomerPriceList extends AbstractAggregateRoot<CustomerPriceList> implements Persistable<CustomerId> {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "customer_id"))
    private CustomerId id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price_list_id"))
    private PriceListId priceListId;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    @Transient
    private boolean isNew;

    public static CustomerPriceList initialize(CustomerId customerId, LocalDateTime createdAt, Username createdBy) {
        CustomerPriceList customerPriceList = new CustomerPriceList();
        customerPriceList.id = customerId;
        customerPriceList.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        customerPriceList.isNew = true;
        return customerPriceList;
    }

    public void changePriceList(PriceListId priceListId, LocalDateTime updatedAt, Username updatedBy) {
        this.priceListId = priceListId;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
}
