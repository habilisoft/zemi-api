package org.habilisoft.zemi.taxesmanagement.tax.application;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRate;

import java.time.LocalDateTime;

public record UpdateTax(TaxId taxId, String name, TaxRate rate, LocalDateTime time, String user) implements Command { }
