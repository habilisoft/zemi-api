package org.habilisoft.zemi.catalog;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public record ProductId(Long value) implements Serializable {
    public static ProductId of(Long value) {
        return new ProductId(value);
    }
}
