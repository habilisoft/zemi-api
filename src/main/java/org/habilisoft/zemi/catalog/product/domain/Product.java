package org.habilisoft.zemi.catalog.product.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Product extends AbstractAggregateRoot<Product> {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ProductId id;
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "category_id"))
    private CategoryId categoryId;
    @Column(name = "is_service")
    private boolean service;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;

    public static Product registerProduct(ProductId id, String name, LocalDateTime createdAt, Username createdBy) {
        return register(id, name, false, createdAt, createdBy);
    }
    public static Product registerService(ProductId id, String name, LocalDateTime createdAt, Username createdBy) {
        return register(id, name, true, createdAt, createdBy);
    }
    private static Product register(ProductId id, String name, boolean isService, LocalDateTime createdAt, Username createdBy) {
        Product product = new Product();
        product.id = id;
        product.name = name;
        product.service = isService;
        product.registerEvent(new ProductRegistered(id));
        product.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return product;
    }

    public void update(LocalDateTime updatedAt, Username updatedBy) {
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
}
