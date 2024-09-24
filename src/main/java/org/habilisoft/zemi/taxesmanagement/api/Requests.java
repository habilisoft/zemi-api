package org.habilisoft.zemi.taxesmanagement.api;

import jakarta.validation.constraints.NotNull;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcSeries;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfType;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;

import java.time.LocalDate;
import java.util.Set;

interface Requests {
    record ChangeNcfType(@NotNull NcfType ncfType) {}
    record CreateTax(@NotNull String name, @NotNull double rate) {}
    record UpdateTax(@NotNull String name, @NotNull double rate) {}
    record AddProductTax(@NotNull Set<TaxId> taxes) {}
    record RemoveProductTax(@NotNull Set<TaxId> taxes) {}
    record AddNcfSequence(@NotNull NcfType ncfType, @NotNull NcSeries series, @NotNull Long start, @NotNull Long end, @NotNull
                          LocalDate expirationDate) {}
}
