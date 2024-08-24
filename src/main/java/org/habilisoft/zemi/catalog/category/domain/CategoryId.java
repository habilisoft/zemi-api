package org.habilisoft.zemi.catalog.category.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public record CategoryId(Long value) implements Serializable {
    public static CategoryId of(Long value) {
        return new CategoryId(value);
    }
}
