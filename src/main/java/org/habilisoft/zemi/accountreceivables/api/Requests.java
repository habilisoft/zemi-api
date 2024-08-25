package org.habilisoft.zemi.accountreceivables.api;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

interface Requests {
    record ChangeCreditLimit(@NotNull BigDecimal creditLimit) { }
}
