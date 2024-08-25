package org.habilisoft.zemi.customer.domain;

import java.io.Serializable;

public record EmailAddress(String value) implements Serializable {
    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }
}