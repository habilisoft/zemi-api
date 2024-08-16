package org.habilisoft.zemi.shared;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record DocumentId(String value) implements Serializable {
    public static DocumentId of(String value) {
        return new DocumentId(value);
    }
}
