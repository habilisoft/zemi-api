package org.habilisoft.zemi.taxesmanagement.tax.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;

@Getter
public class TaxNotFound extends DomainException {
    private final TaxId taxId;
    public TaxNotFound(TaxId taxId) {
        super("Tax %s not found".formatted(taxId.value()));
        this.taxId = taxId;
    }
}
