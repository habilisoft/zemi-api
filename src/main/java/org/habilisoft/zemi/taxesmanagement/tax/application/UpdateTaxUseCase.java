package org.habilisoft.zemi.taxesmanagement.tax.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.tax.domain.Tax;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxNotFound;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRepository;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateTaxUseCase implements UseCase<UpdateTax, Void> {
    private final TaxRepository taxRepository;
    @Override
    public Void execute(UpdateTax updateTax) {
        TaxId taxId = updateTax.taxId();
        Tax tax = taxRepository.findById(taxId)
                .orElseThrow(() -> new TaxNotFound(taxId));
        String user = updateTax.user();
        LocalDateTime time = updateTax.time();
        tax.update(updateTax.name(), updateTax.rate(), time, Username.of(user));
        taxRepository.save(tax);
        return null;
    }
}
