package org.habilisoft.zemi.catalog;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.AbstractAggregateRoot;
@Data
@Entity
@Table(name = "categories")
@EqualsAndHashCode(of = "id", callSuper = false)
class Category extends AbstractAggregateRoot<Category> {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private CategoryId id;
    private String name;

    static Category create(CategoryId id, String name) {
        Category category = new Category();
        category.id = id;
        category.name = name;
        return category;
    }
}
