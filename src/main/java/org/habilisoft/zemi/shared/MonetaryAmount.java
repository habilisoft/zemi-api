package org.habilisoft.zemi.shared;

import java.math.BigDecimal;

public record MonetaryAmount(BigDecimal value){
    public static MonetaryAmount ZERO = new MonetaryAmount(BigDecimal.ZERO);
    public static MonetaryAmount of(BigDecimal value){
        return new MonetaryAmount(value);
    }

    public MonetaryAmount add(MonetaryAmount other){
        return new MonetaryAmount(value.add(other.value));
    }

    public MonetaryAmount multiply(Quantity quantity){
        return new MonetaryAmount(value.multiply(quantity.value()));
    }
    public MonetaryAmount multiply(double quantity){
        return new MonetaryAmount(value.multiply(BigDecimal.valueOf(quantity)));
    }
}
