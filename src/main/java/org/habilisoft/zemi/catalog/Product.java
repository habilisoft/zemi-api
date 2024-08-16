package org.habilisoft.zemi.catalog;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.AbstractAggregateRoot;

@Data
@Entity
@Table(name = "products")
@EqualsAndHashCode(of = "id", callSuper = false)
class Product extends AbstractAggregateRoot<Product> {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ProductId id;
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "category_id"))
    private CategoryId categoryId;

    static Product register(ProductId id, String name) {
        Product product = new Product();
        product.id = id;
        product.name = name;
        product.registerEvent(new ProductRegistered(id));
        return product;
    }
}
