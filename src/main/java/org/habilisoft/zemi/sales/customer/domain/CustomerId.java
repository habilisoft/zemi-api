package org.habilisoft.zemi.sales.customer.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public record CustomerId(Long value) implements Serializable {
    public static CustomerId of(Long value) {
        return new CustomerId(value);
    }
}
