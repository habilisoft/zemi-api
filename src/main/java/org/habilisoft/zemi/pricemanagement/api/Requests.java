package org.habilisoft.zemi.pricemanagement.api;

import java.math.BigDecimal;

interface Requests {
    record ChangeProductPrice(BigDecimal price) { }
}
