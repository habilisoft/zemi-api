package org.habilisoft.zemi.catalog.category.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "categories")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Category extends AbstractAggregateRoot<Category> {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private CategoryId id;
    private String name;
    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;

    public static Category create(CategoryId id, String name, LocalDateTime createdAt, Username createdBy) {
        Category category = new Category();
        category.id = id;
        category.name = name;
        category.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return category;
    }

    public void update(String name, LocalDateTime updatedAt, Username updatedBy) {
        this.name = name;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
}
