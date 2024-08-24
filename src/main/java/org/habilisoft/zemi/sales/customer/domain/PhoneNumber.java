package org.habilisoft.zemi.sales.customer.domain;

import java.io.Serializable;

public record PhoneNumber(String value) implements Serializable {
    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}