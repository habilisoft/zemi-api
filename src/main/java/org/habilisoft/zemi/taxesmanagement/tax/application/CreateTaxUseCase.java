package org.habilisoft.zemi.taxesmanagement.tax.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.tax.domain.Tax;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxIdGenerator;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRepository;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateTaxUseCase implements UseCase<CreateTax, TaxId> {
    private final TaxRepository taxRepository;
    private final TaxIdGenerator taxIdGenerator;
    @Override
    public TaxId execute(CreateTax createTax) {
        TaxId taxId = taxIdGenerator.generate();
        String user = createTax.user();
        LocalDateTime time = createTax.time();
        Tax tax = Tax.newTax(taxId, createTax.name(), createTax.rate(), time, Username.of(user));
        taxRepository.save(tax);
        return taxId;
    }
}
