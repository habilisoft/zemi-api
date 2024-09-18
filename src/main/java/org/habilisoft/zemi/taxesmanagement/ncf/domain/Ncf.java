package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record Ncf(String value) {
    public Ncf {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ncf value cannot be null or empty");
        }
        if (value.length() != 11)
            throw new IllegalArgumentException("Ncf value must be 11 characters long");

        NcSeries.valueOf(String.valueOf(value.charAt(0)));
        NcfType.valueOf(value.substring(1, 2));
        if (!value.substring(2).chars().allMatch(Character::isDigit))
            throw new IllegalArgumentException("Ncf value must contain only digits after the first two characters");
    }
}
