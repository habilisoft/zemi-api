package org.habilisoft.zemi.user;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record Username(String value) implements Serializable {
    public static Username of(String value) {
        return new Username(value);
    }
}
