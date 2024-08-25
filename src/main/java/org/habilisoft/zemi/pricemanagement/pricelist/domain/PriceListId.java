package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import jakarta.persistence.Embeddable;
import org.springframework.modulith.NamedInterface;

import java.io.Serializable;
@Embeddable
@NamedInterface
public record PriceListId(Long value) implements Serializable {
    public static PriceListId of(Long value) {
        return new PriceListId(value);
    }
}
