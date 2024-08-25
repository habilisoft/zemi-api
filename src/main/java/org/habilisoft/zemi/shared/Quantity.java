package org.habilisoft.zemi.shared;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
@Embeddable
public record Quantity(BigDecimal value) {
    public Quantity {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be greater than or equal to zero");
        }
    }
}
