package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import jakarta.persistence.Embeddable;
import org.springframework.modulith.NamedInterface;

import java.util.Objects;

@Embeddable
@NamedInterface
public record Ncf(String value) {
    public Ncf {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ncf value cannot be null or empty");
        }
        if (value.length() != 11)
            throw new IllegalArgumentException("Ncf value must be 11 characters long");
        Objects.requireNonNull(NcfType.of(value.substring(1, 3)));
        Objects.requireNonNull(String.valueOf(value.charAt(0)));
        if (!value.substring(3).chars().allMatch(Character::isDigit))
            throw new IllegalArgumentException("Ncf value must contain only digits after the first two characters");
    }

    public NcfType ncfType() {
        return NcfType.of(value.substring(1, 3));
    }
    public NcSeries series() {
        return NcSeries.of(String.valueOf(value.charAt(0)));
    }
    public String sequence() {
        return value.substring(3);
    }
}
