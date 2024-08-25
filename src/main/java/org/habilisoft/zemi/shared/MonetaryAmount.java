package org.habilisoft.zemi.shared;

import java.math.BigDecimal;

public record MonetaryAmount(BigDecimal value){
    public static MonetaryAmount of(BigDecimal value){
        return new MonetaryAmount(value);
    }
}
