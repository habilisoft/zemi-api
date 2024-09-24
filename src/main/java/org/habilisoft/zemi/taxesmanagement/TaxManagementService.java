package org.habilisoft.zemi.taxesmanagement;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.customer.domain.CustomerRegistered;
import org.habilisoft.zemi.taxesmanagement.application.ChangeCustomerNcfType;
import org.habilisoft.zemi.taxesmanagement.application.InitializeCustomerTax;
import org.habilisoft.zemi.taxesmanagement.customer.application.ChangeCustomerNcfTypeUseCase;
import org.habilisoft.zemi.taxesmanagement.customer.application.InitializeCustomerTaxUseCase;
import org.habilisoft.zemi.taxesmanagement.ncf.application.AddNcfSequence;
import org.habilisoft.zemi.taxesmanagement.ncf.application.AddNcfSequenceUseCase;
import org.habilisoft.zemi.taxesmanagement.ncf.application.GenerateNcf;
import org.habilisoft.zemi.taxesmanagement.ncf.application.GenerateNcfUseCase;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.Ncf;
import org.habilisoft.zemi.taxesmanagement.product.application.AddProductTaxes;
import org.habilisoft.zemi.taxesmanagement.product.application.AddProductTaxesUseCase;
import org.habilisoft.zemi.taxesmanagement.product.application.RemoveProductTaxes;
import org.habilisoft.zemi.taxesmanagement.product.application.RemoveProductTaxesUseCase;
import org.habilisoft.zemi.taxesmanagement.product.domain.ProductIdAndTax;
import org.habilisoft.zemi.taxesmanagement.product.domain.ProductTaxRepository;
import org.habilisoft.zemi.taxesmanagement.product.domain.TaxIdAndRate;
import org.habilisoft.zemi.taxesmanagement.tax.application.CreateTax;
import org.habilisoft.zemi.taxesmanagement.tax.application.CreateTaxUseCase;
import org.habilisoft.zemi.taxesmanagement.tax.application.UpdateTax;
import org.habilisoft.zemi.taxesmanagement.tax.application.UpdateTaxUseCase;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class TaxManagementService {
    private final InitializeCustomerTaxUseCase initializeCustomerTaxUseCase;
    private final ChangeCustomerNcfTypeUseCase changeCustomerNcfTypeUseCase;
    private final CreateTaxUseCase createTaxUseCase;
    private final UpdateTaxUseCase updateTaxUseCase;
    private final AddProductTaxesUseCase addProductTaxesUseCase;
    private final RemoveProductTaxesUseCase removeProductTaxesUseCase;
    private final ProductTaxRepository productTaxRepository;
    private final GenerateNcfUseCase generateNcfUseCase;
    private final AddNcfSequenceUseCase addNcfSequenceUseCase;

    @ApplicationModuleListener
    void on(CustomerRegistered customerRegistered) {
        initializeCustomerTaxUseCase.execute(
                new InitializeCustomerTax(
                        customerRegistered.customerId(),
                        customerRegistered.time(),
                        customerRegistered.user())
        );
    }

    public void changeCustomerNcfType(ChangeCustomerNcfType changeCustomerNcfType) {
        changeCustomerNcfTypeUseCase.execute(changeCustomerNcfType);
    }

    public TaxId createTax(CreateTax createTax) {
        return createTaxUseCase.execute(createTax);
    }

    public void updateTax(UpdateTax updateTax) {
        updateTaxUseCase.execute(updateTax);
    }

    public void addProductTaxes(AddProductTaxes addProductTaxes) {
        addProductTaxesUseCase.execute(addProductTaxes);
    }

    public void removeProductTaxes(RemoveProductTaxes removeProductTaxes) {
        removeProductTaxesUseCase.execute(removeProductTaxes);
    }

    public Map<ProductId, Set<TaxIdAndRate>> getProductTaxes(Set<ProductId> productIds) {
        return productTaxRepository.findTaxesByProductIds(productIds)
                .stream()
                .collect(groupingBy(ProductIdAndTax::getProductId, Collectors.mapping(productIdAndTax -> TaxIdAndRate.from(productIdAndTax.getTax()), toSet())));
    }

    public void addNcfSequence(AddNcfSequence addNcfSequence) {
        addNcfSequenceUseCase.execute(addNcfSequence);
    }

    public Ncf generateNcfForCustomer(CustomerId customerId, String generatedBy, LocalDateTime generatedAt) {
        return generateNcfUseCase.execute(new GenerateNcf.ForCustomer(customerId, generatedAt, generatedBy));
    }
}
