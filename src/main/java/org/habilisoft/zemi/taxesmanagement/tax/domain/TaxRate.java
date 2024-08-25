package org.habilisoft.zemi.taxesmanagement.tax.domain;

import org.habilisoft.zemi.shared.MonetaryAmount;
import org.springframework.modulith.NamedInterface;

@NamedInterface
public record TaxRate(double value) {
    public TaxRate {
        if(value > 1){
            value = value / 100;
        }
    }
    public static TaxRate fromPercentage(double percentage){
        return new TaxRate(percentage);
    }

    public MonetaryAmount calculateTax(MonetaryAmount amount){
        return amount.multiply(value);
    }
}
