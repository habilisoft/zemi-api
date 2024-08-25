package org.habilisoft.zemi.catalog.product.domain;

import jakarta.persistence.Embeddable;
import org.springframework.modulith.NamedInterface;

import java.io.Serializable;
@Embeddable
@NamedInterface
public record ProductId(Long value) implements Serializable {
    public static ProductId of(Long value) {
        return new ProductId(value);
    }
}
