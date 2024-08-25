package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
@Getter
public class PriceListNotFound extends DomainException {
    private final PriceListId priceListId;
    public PriceListNotFound(PriceListId priceListId) {
        super("Price list %s not found".formatted(priceListId.value()));
        this.priceListId = priceListId;
    }
}
