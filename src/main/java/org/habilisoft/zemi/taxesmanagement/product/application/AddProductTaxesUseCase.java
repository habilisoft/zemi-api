package org.habilisoft.zemi.taxesmanagement.product.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductNotFound;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.product.domain.Product;
import org.habilisoft.zemi.taxesmanagement.product.domain.ProductTaxRepository;
import org.habilisoft.zemi.taxesmanagement.tax.domain.Tax;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxNotFound;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRepository;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddProductTaxesUseCase implements UseCase<AddProductTaxes, Void> {
    private final ProductTaxRepository productTaxRepository;
    private final TaxRepository taxRepository;

    @Override
    public Void execute(AddProductTaxes addProductTaxes) {
        ProductId productId = addProductTaxes.productId();
        Product product = productTaxRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFound(productId));

        Map<TaxId, Tax> taxMap = taxRepository.findAllById(addProductTaxes.taxIds())
                .stream()
                .collect(Collectors.toMap(Tax::getId, Function.identity()));

        addProductTaxes.taxIds().stream().filter(taxId -> !taxMap.containsKey(taxId))
                .findAny()
                .ifPresent(taxId -> {
                    throw new TaxNotFound(taxId);
                });

        product.addTaxes(addProductTaxes.taxIds(), addProductTaxes.time(), Username.of(addProductTaxes.user()));
        productTaxRepository.save(product);
        return null;
    }
}
