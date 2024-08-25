package org.habilisoft.zemi.taxesmanagement.product.domain;

import org.habilisoft.zemi.taxesmanagement.tax.domain.Tax;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRate;
import org.springframework.modulith.NamedInterface;

@NamedInterface
public record TaxIdAndRate(TaxId taxId, TaxRate taxRate) {
    public static TaxIdAndRate from(Tax tax){
        return new TaxIdAndRate(tax.getId(), tax.getRate());
    }
}
