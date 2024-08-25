package org.habilisoft.zemi.taxesmanagement.tax.domain;

import jakarta.persistence.Embeddable;
import org.springframework.modulith.NamedInterface;

import java.io.Serializable;
@Embeddable
@NamedInterface
public record TaxId(Long value) implements Serializable {
    public static TaxId of(Long value) {
        return new TaxId(value);
    }
}
