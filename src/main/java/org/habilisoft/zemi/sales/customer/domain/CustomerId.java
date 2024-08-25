package org.habilisoft.zemi.sales.customer.domain;

import jakarta.persistence.Embeddable;
import org.springframework.modulith.NamedInterface;

import java.io.Serializable;
@Embeddable
@NamedInterface
public record CustomerId(Long value) implements Serializable {
    public static CustomerId of(Long value) {
        return new CustomerId(value);
    }
}
