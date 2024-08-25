package org.habilisoft.zemi.taxesmanagement.product.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.product.domain.Product;
import org.habilisoft.zemi.taxesmanagement.product.domain.ProductTaxNotFound;
import org.habilisoft.zemi.taxesmanagement.product.domain.ProductTaxRepository;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveProductTaxesUseCase implements UseCase<RemoveProductTaxes, Void> {
    private final ProductTaxRepository productTaxRepository;
    @Override
    public Void execute(RemoveProductTaxes removeProductTaxes) {
        ProductId productId = removeProductTaxes.productId();
        Product product = productTaxRepository.findById(productId)
                .orElseThrow(() -> new ProductTaxNotFound(productId));
        product.removeTax(removeProductTaxes.taxIds(), removeProductTaxes.time(), Username.of(removeProductTaxes.user()));
        productTaxRepository.save(product);
        return null;
    }
}
