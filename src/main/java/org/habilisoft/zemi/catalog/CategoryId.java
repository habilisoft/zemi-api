package org.habilisoft.zemi.catalog;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public record CategoryId(Long value) implements Serializable {
    public static CategoryId of(Long value) {
        return new CategoryId(value);
    }
}
