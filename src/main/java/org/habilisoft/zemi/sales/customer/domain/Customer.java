package org.habilisoft.zemi.sales.customer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(of="id", callSuper = false)
@Table(name = "customers")
public class Customer extends AbstractAggregateRoot<Customer> implements Persistable<CustomerId> {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private CustomerId id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CustomerType type;
    @ElementCollection
    @CollectionTable(name = "business_entity_addresses", joinColumns = @JoinColumn(name = "business_entity_id"))
    private Set<Address> address;

    @ElementCollection
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.PRIVATE)
    @AttributeOverride(name = "value", column = @Column(name = "phone"))
    @CollectionTable(name = "business_entity_phones", joinColumns = @JoinColumn(name = "business_entity_id"))
    private Set<PhoneNumber> phoneNumbers;

    @Embedded
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.PRIVATE)
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private EmailAddress emailAddress;

    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;

    @Transient
    boolean isNew;

    public static Customer register(CustomerId id, String name, CustomerType type,  Address address, Contact contact, LocalDateTime createdAt, Username createdBy) {
        Customer customer = new Customer();
        customer.id = id;
        customer.name = name;
        customer.type = type;
        customer.address = Set.of(address);
        customer.phoneNumbers = contact.phoneNumbers();
        customer.emailAddress = contact.emailAddress();
        customer.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        customer.registerEvent(new CustomerRegistered(id));
        customer.isNew = true;
        return customer;
    }

    public void changeAddress(Address newAddress, LocalDateTime updatedAt, Username updatedBy) {
        this.address = Set.of(newAddress);
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
        registerEvent(new CustomerAddressChanged(id, newAddress));
    }

    public void changeContact(Contact newContact, LocalDateTime updatedAt, Username updatedBy) {
        this.phoneNumbers = newContact.phoneNumbers();
        this.emailAddress = newContact.emailAddress();
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
        registerEvent(new CustomerContactChanged(id, newContact));
    }

    public void changeCreditLimit(MonetaryAmount creditLimit) {
        registerEvent(new CustomerCreditLimitUpdated(id, creditLimit));
    }
    public void changeNcfType(NcfType ncfType) {
        registerEvent(new CustomerNcfTypeChanged(id, ncfType));
    }
}
