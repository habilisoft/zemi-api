package org.habilisoft.zemi.taxesmanagement.customer.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.application.ChangeCustomerNcfType;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTax;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTaxNotFound;
import org.habilisoft.zemi.taxesmanagement.customer.domain.CustomerTaxRepository;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChangeCustomerNcfTypeUseCase implements UseCase<ChangeCustomerNcfType, Void> {
    private final CustomerTaxRepository customerTaxRepository;
    @Override
    public Void execute(ChangeCustomerNcfType changeCustomerNcfType) {
        LocalDateTime updatedAt = changeCustomerNcfType.time();
        Username updatedBy = Username.of(changeCustomerNcfType.user());
        CustomerTax customerTax = customerTaxRepository.findById(changeCustomerNcfType.customerId())
                .orElseThrow(() -> new CustomerTaxNotFound(changeCustomerNcfType.customerId()));
        customerTax.changeNcfType(changeCustomerNcfType.ncfType(), updatedAt, updatedBy);
        customerTaxRepository.save(customerTax);
        return null;
    }
}
