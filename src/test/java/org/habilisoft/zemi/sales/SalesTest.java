package org.habilisoft.zemi.sales;

import com.jayway.jsonpath.JsonPath;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.customer.domain.Customer;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceList;
import org.habilisoft.zemi.sales.sale.domain.Sale;
import org.habilisoft.zemi.sales.sale.domain.SaleProduct;
import org.habilisoft.zemi.shared.DocumentId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.Quantity;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.Tax;
import org.habilisoft.zemi.util.Commands;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Sales Test")
class SalesTest extends AbstractIt {

    private final DocumentId salesDocument = DocumentId.of("S");
    private final String path = "/sales/v1";
    private final String pizzaPrice = "915.00";
    private final String sodaPrice = "115.00";

    @Test
    @DisplayName("Should make a sale with product price")
    void shouldMakeASale() throws Exception {
        // Given
        Customer johnDoe = customerFixtures.customer1();
        Product pizza = catalogFixtures.product1();
        Product soda = catalogFixtures.product2();
        Tax tax18Percent = taxManagementFixtures.tax18Percent();

        addTaxToProduct(tax18Percent, pizza);
        addTaxToProduct(tax18Percent, soda);

        changePriceToProduct(pizza, pizzaPrice);
        changePriceToProduct(soda, sodaPrice);

        assert johnDoe.getId() != null;
        Map<String, Object> makeSale = Map.of(
                "documentId", salesDocument.value(),
                "customerId", johnDoe.getId().value(),
                "products", List.of(
                        Map.of("productId", pizza.getId().value(), "quantity", 1),
                        Map.of("productId", soda.getId().value(), "quantity", 2)
                )
        );
        var jwtToken = jwtToken(username);
        // When
        MvcResult mvcResult = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(makeSale))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(salesContext.saleRepository.findById(TransactionalId.of(salesDocument, id.longValue())))
                .isPresent()
                .hasValueSatisfying(sale -> assertSale(sale, johnDoe, pizza, soda, tax18Percent));
    }

    @Test
    @DisplayName("Should make a sale with product price from price list")
    void shouldMakeASaleWithPriceList() throws Exception {
        // Given
        Customer johnDoe = customerFixtures.customer1();
        Product pizza = catalogFixtures.product1();
        Product soda = catalogFixtures.product2();
        Tax tax18Percent = taxManagementFixtures.tax18Percent();

        addTaxToProduct(tax18Percent, pizza);
        addTaxToProduct(tax18Percent, soda);

        PriceList priceList = priceManagementFixtures.priceList3();
        changePriceToProduct(pizza, priceList, pizzaPrice);
        changePriceToProduct(soda, priceList, sodaPrice);

        changeCustomerPriceList(priceList, johnDoe);

        assert johnDoe.getId() != null;
        Map<String, Object> makeSale = Map.of(
                "documentId", salesDocument.value(),
                "customerId", johnDoe.getId().value(),
                "products", List.of(
                        Map.of("productId", pizza.getId().value(), "quantity", 1),
                        Map.of("productId", soda.getId().value(), "quantity", 2)
                )
        );
        var jwtToken = jwtToken(username);
        // When
        MvcResult mvcResult = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(makeSale))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(salesContext.saleRepository.findById(TransactionalId.of(salesDocument, id.longValue())))
                .isPresent()
                .hasValueSatisfying(sale -> assertSale(sale, johnDoe, pizza, soda, tax18Percent));
    }

    @Test
    @DisplayName("Should make a sale with product price from price list and product price")
    void shouldMakeASaleWithPriceListAndProductPrice() throws Exception {
        // Given
        Customer johnDoe = customerFixtures.customer1();
        Product pizza = catalogFixtures.product1();
        Product soda = catalogFixtures.product2();
        Tax tax18Percent = taxManagementFixtures.tax18Percent();

        addTaxToProduct(tax18Percent, pizza);
        addTaxToProduct(tax18Percent, soda);

        PriceList priceList = priceManagementFixtures.priceList3();
        changePriceToProduct(pizza, priceList, pizzaPrice);
        changePriceToProduct(soda, sodaPrice);
        assert johnDoe.getId() != null;
        await().until(() -> priceManagementContext.customerPriceListRepository.findById(johnDoe.getId()).isPresent());
        changeCustomerPriceList(priceList, johnDoe);
        assert johnDoe.getId() != null;
        Map<String, Object> makeSale = Map.of(
                "documentId", salesDocument.value(),
                "customerId", johnDoe.getId().value(),
                "products", List.of(
                        Map.of("productId", pizza.getId().value(), "quantity", 1),
                        Map.of("productId", soda.getId().value(), "quantity", 2)
                )
        );
        var jwtToken = jwtToken(username);
        // When
        MvcResult mvcResult = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(makeSale))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(salesContext.saleRepository.findById(TransactionalId.of(salesDocument, id.longValue())))
                .isPresent()
                .hasValueSatisfying(sale -> assertSale(sale, johnDoe, pizza, soda, tax18Percent));
    }

    @Test
    @DisplayName("Should make a sale with product price manually set")
    void shouldMakeASaleWithProductPriceManuallySet() throws Exception {
        // Given
        Customer johnDoe = customerFixtures.customer1();
        Product pizza = catalogFixtures.product1();
        Product soda = catalogFixtures.product2();
        Tax tax18Percent = taxManagementFixtures.tax18Percent();

        addTaxToProduct(tax18Percent, pizza);
        addTaxToProduct(tax18Percent, soda);

        assert johnDoe.getId() != null;
        Map<String, Object> makeSale = Map.of(
                "documentId", salesDocument.value(),
                "customerId", johnDoe.getId().value(),
                "products", List.of(
                        Map.of("productId", pizza.getId().value(), "quantity", 1, "price", pizzaPrice),
                        Map.of("productId", soda.getId().value(), "quantity", 2, "price", sodaPrice)
                )
        );
        var jwtToken = jwtToken(username);
        // When
        MvcResult mvcResult = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(makeSale))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(salesContext.saleRepository.findById(TransactionalId.of(salesDocument, id.longValue())))
                .isPresent()
                .hasValueSatisfying(sale -> assertSale(sale, johnDoe, pizza, soda, tax18Percent));
    }

    private void assertSale(Sale sale, Customer johnDoe, Product pizza, Product soda, Tax tax18Percent) {
        assertThat(sale.getCustomerId()).isEqualTo(johnDoe.getId());
        assertThat(sale.getTotal()).isEqualTo(MonetaryAmount.of(new BigDecimal("1351.10")));
        assertThat(sale.getProducts()).hasSize(2);
        assertThat(sale.getProducts().stream().filter(saleItem -> saleItem.productId().equals(pizza.getId())).findFirst())
                .isPresent()
                .hasValueSatisfying(saleItem -> assertSaleProduct(
                        saleItem,
                        pizza,
                        tax18Percent,
                        "1",
                        pizzaPrice,
                        "164.70",
                        "1079.70"
                ));
        assertThat(sale.getProducts().stream().filter(saleItem -> saleItem.productId().equals(soda.getId())).findFirst())
                .isPresent()
                .hasValueSatisfying(saleProduct -> assertSaleProduct(
                        saleProduct,
                        soda,
                        tax18Percent,
                        "2",
                        sodaPrice,
                        "20.70",
                        "271.40"
                ));
    }

    private static void assertSaleProduct(
            SaleProduct saleProduct,
            Product product,
            Tax tax,
            String expectedQuantity,
            String expectedPriceAmount,
            String expectedTaxAmount,
            String expectedTotalAmount
    ) {
        assertThat(saleProduct.productId()).isEqualTo(product.getId());
        assertThat(saleProduct.getQuantity()).isEqualTo(Quantity.of(new BigDecimal(expectedQuantity)));
        assertThat(saleProduct.getPrice()).isEqualTo(MonetaryAmount.of(new BigDecimal(expectedPriceAmount)));
        assertThat(saleProduct.getTaxAmount()).isEqualTo(MonetaryAmount.of(new BigDecimal(expectedTaxAmount)));
        assertThat(saleProduct.total()).isEqualTo(MonetaryAmount.of(new BigDecimal(expectedTotalAmount)));
        assertThat(saleProduct.getTaxes()).hasSize(1);
        assertThat(saleProduct.getTaxes()).anySatisfy(saleProductTax -> {
            assertThat(saleProductTax.taxId()).isEqualTo(tax.getId());
            assertThat(saleProductTax.getTaxRate()).isEqualTo(tax.getRate());
            assertThat(saleProductTax.getTaxAmount()).isEqualTo(MonetaryAmount.of(new BigDecimal(expectedTaxAmount)));
        });
    }

    private void changePriceToProduct(Product product, String price) {
        var changeProductPrice = Commands.PriceManagement.changeProductPriceBuilder().productId(product.getId()).price(price).build();
        priceManagementContext.priceManagementService.changeProductPrice(changeProductPrice);
    }

    private void changePriceToProduct(Product product, PriceList priceList, String price) {
        var addProductsToPriceList = Commands.PriceManagement.addProductsToPriceListBuilder()
                .priceListId(priceList.getId())
                .productId(product.getId())
                .price(price)
                .build();
        priceManagementContext.priceManagementService.addProductsToPriceList(addProductsToPriceList);
    }

    private void changeCustomerPriceList(PriceList priceList, Customer customer) {
        var changeCustomerPriceList = Commands.PriceManagement.changeCustomerPriceListBuilder()
                .priceListId(priceList.getId())
                .customerId(customer.getId())
                .build();
        priceManagementContext.priceManagementService.changeCustomerPriceList(changeCustomerPriceList);
    }

    private void addTaxToProduct(Tax tax18Percent, Product pizza) {
        assert tax18Percent.getId() != null;
        var addProductTaxes = Commands.TaxManagement.addProductTaxesBuilder()
                .taxes(Set.of(tax18Percent.getId()))
                .productId(pizza.getId()).build();
        taxManagementContext.taxManagementService.addProductTaxes(addProductTaxes);
    }
}
