package org.habilisoft.zemi.taxesmanagement.ncf.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTax;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTaxNotFound;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTaxRepository;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.*;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateNcfUseCase implements UseCase<GenerateNcf, Ncf> {
    private final NcfSequenceRepository ncfSequenceRepository;
    private final CustomerTaxRepository customerTaxRepository;
    @Override
    public Ncf execute(GenerateNcf generateNcf) {
        NcfSequence ncfSequence =  switch (generateNcf) {
            case GenerateNcf.ForCustomer(var customerId, _, _) -> generateNcfForCustomer(customerId);
            case GenerateNcf.ForType(var type, _, _) -> generateNcfForType(type);
        };
        Ncf ncf = ncfSequence.increment(generateNcf.time(), Username.of(generateNcf.user()));
        ncfSequenceRepository.save(ncfSequence);
        return ncf;
    }

    private NcfSequence generateNcfForCustomer(CustomerId customerId) {
        CustomerTax customerTax = customerTaxRepository.findById(customerId)
                .orElseThrow(() -> new CustomerTaxNotFound(customerId));
        return generateNcfForType(customerTax.getNcfType());
    }

    private NcfSequence generateNcfForType(NcfType type) {
        return ncfSequenceRepository.findByNcfType(type)
                .orElseThrow(() -> new NcfSequenceNotFoundException(type));
    }
}
