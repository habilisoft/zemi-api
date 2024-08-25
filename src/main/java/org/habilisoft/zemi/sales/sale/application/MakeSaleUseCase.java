package org.habilisoft.zemi.sales.sale.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.CatalogService;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductNotFound;
import org.habilisoft.zemi.pricemanagement.PriceManagementService;
import org.habilisoft.zemi.sales.customer.domain.CustomerNotFound;
import org.habilisoft.zemi.sales.customer.domain.CustomerRepository;
import org.habilisoft.zemi.sales.sale.domain.Sale;
import org.habilisoft.zemi.sales.sale.domain.SaleProduct;
import org.habilisoft.zemi.sales.sale.domain.SaleRepository;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.shared.TransactionalIdGenerator;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.TaxManagementService;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxRate;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MakeSaleUseCase implements UseCase<MakeSale, TransactionalId> {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final TransactionalIdGenerator transactionalIdGenerator;
    private final CatalogService catalogService;
    private final TaxManagementService taxManagementService;
    private final PriceManagementService priceManagementService;

    @Override
    @Transactional
    public TransactionalId execute(MakeSale makeSale) {
        TransactionalId transactionalId = transactionalIdGenerator.generate(makeSale.documentId());
        makeSale.customerId().filter(customerId -> {
            if (customerRepository.existsById(customerId)) {
                return true;
            }
            throw new CustomerNotFound(customerId);
        });
        Set<ProductId> productIds = makeSale.products().stream().map(MakeSale.Product::productId).collect(Collectors.toSet());
        Map<ProductId, Boolean> existenceMap = catalogService.productExists(productIds);
        existenceMap.entrySet().stream().filter(Predicate.not(Map.Entry::getValue)).findAny().ifPresent(entry -> {
            throw new ProductNotFound(entry.getKey());
        });
        Map<ProductId, MonetaryAmount> productPrices = priceManagementService.getCurrentPrice(productIds);
        Map<ProductId, Set<TaxRate>> productTaxes = taxManagementService.getProductTaxes(productIds);
        Set<SaleProduct> products = makeSale.products().stream()
                .map(product -> {
                    MonetaryAmount price = productPrices.getOrDefault(product.productId(), MonetaryAmount.ZERO);
                    Set<TaxRate> taxRates = productTaxes.get(product.productId());
                    return SaleProduct.of(transactionalId, product.productId(), product.quantity(), price, taxRates);
                })
                .collect(Collectors.toSet());
        LocalDateTime createdAt = makeSale.time();
        Username createdBy = Username.of(makeSale.user());
        Sale sale = makeSale.customerId().map(customerId -> Sale.makeSaleForCustomer(transactionalId, customerId, products, createdAt, createdBy))
                .orElseGet(() -> Sale.makeSale(transactionalId, products, createdAt, createdBy));
        saleRepository.save(sale);
        return transactionalId;
    }
}
