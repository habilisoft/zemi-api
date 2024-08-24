package org.habilisoft.zemi.sales.customer.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public record PhoneNumber(String value) implements Serializable {
    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}