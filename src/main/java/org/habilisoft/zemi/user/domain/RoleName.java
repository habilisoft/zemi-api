package org.habilisoft.zemi.user.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record RoleName(String value) implements Serializable {
    public static RoleName from(String value) {
        return new RoleName(value);
    }
}