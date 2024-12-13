package org.habilisoft.zemi.sales.sale.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.CatalogService;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.catalog.product.domain.ProductNotFound;
import org.habilisoft.zemi.customer.CustomerService;
import org.habilisoft.zemi.customer.domain.CustomerNotFound;
import org.habilisoft.zemi.pricemanagement.PriceManagementService;
import org.habilisoft.zemi.sales.sale.domain.Sale;
import org.habilisoft.zemi.sales.sale.domain.SaleProduct;
import org.habilisoft.zemi.sales.sale.domain.SaleRepository;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.shared.TransactionalIdGenerator;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.TaxManagementService;
import org.habilisoft.zemi.taxesmanagement.product.domain.TaxIdAndRate;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MakeSaleUseCase implements UseCase<MakeSale, TransactionalId> {
    private final SaleRepository saleRepository;
    private final CustomerService customerService;
    private final TransactionalIdGenerator transactionalIdGenerator;
    private final CatalogService catalogService;
    private final TaxManagementService taxManagementService;
    private final PriceManagementService priceManagementService;

    @Override
    @Transactional
    public TransactionalId execute(MakeSale makeSale) {
        TransactionalId transactionalId = transactionalIdGenerator.generate(makeSale.documentId());
        makeSale.customerId().ifPresent(customerId -> {
            if (!customerService.exists(customerId)) {
                throw new CustomerNotFound(customerId);
            }
        });
        Set<ProductId> productIds = makeSale.products().stream().map(MakeSale.Product::productId).collect(Collectors.toSet());
        Map<ProductId, Boolean> existenceMap = catalogService.productExists(productIds);
        existenceMap.entrySet().stream().filter(Predicate.not(Map.Entry::getValue)).findAny().ifPresent(entry -> {
            throw new ProductNotFound(entry.getKey());
        });
        Map<ProductId, MonetaryAmount> productPrices = productPrices(makeSale);
        Map<ProductId, Set<TaxIdAndRate>> productTaxes = taxManagementService.getProductTaxes(productIds);
        Set<SaleProduct> products = makeSale.products().stream()
                .map(product -> {
                    MonetaryAmount price = productPrices.getOrDefault(product.productId(), MonetaryAmount.ZERO);
                    Set<TaxIdAndRate> taxRates = productTaxes.get(product.productId());
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

    Map<ProductId, MonetaryAmount> productPrices(MakeSale makeSale) {
        Set<MakeSale.Product> products = makeSale.products();

        Set<ProductId> productIds = products.stream().map(MakeSale.Product::productId).collect(Collectors.toSet());

        Map<ProductId, MonetaryAmount> priceMap = products.stream().filter(product -> product.price().isPresent())
                .collect(Collectors.toMap(MakeSale.Product::productId, product -> product.price().get()));

        Supplier<Set<ProductId>> productsWithoutPrice = () ->
                productIds.stream().filter(productId -> !priceMap.containsKey(productId)).collect(Collectors.toSet());

        makeSale.customerId().ifPresent(customerId -> {
            Map<ProductId, MonetaryAmount> priceByCustomer = priceManagementService.getCurrentPrice(customerId, productsWithoutPrice.get());
            priceMap.putAll(priceByCustomer);
        });

        Map<ProductId, MonetaryAmount> priceByProduct = priceManagementService.getCurrentPrice(productsWithoutPrice.get());
        priceMap.putAll(priceByProduct);
        return priceMap;

    }

}
