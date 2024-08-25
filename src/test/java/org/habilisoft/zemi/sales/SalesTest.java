package org.habilisoft.zemi.sales;

import com.jayway.jsonpath.JsonPath;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.customer.domain.Customer;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Sales Test")
class SalesTest extends AbstractIt {

    @Test
    @DisplayName("Should make a sale")
    void shouldMakeASale() throws Exception {
        // Given
        Customer johnDoe = customerFixtures.customer1();
        Product pizza = catalogFixtures.product1();
        Product soda = catalogFixtures.product2();

        Tax tax18Percent = taxManagementFixtures.tax18Percent();
        assert tax18Percent.getId() != null;
        var addProductTaxesBuilder = Commands.TaxManagement.addProductTaxesBuilder().taxes(Set.of(tax18Percent.getId()));
        taxManagementContext.taxManagementService.addProductTaxes(addProductTaxesBuilder.productId(pizza.getId()).build());
        taxManagementContext.taxManagementService.addProductTaxes(addProductTaxesBuilder.productId(soda.getId()).build());

        var pizzaPrice = Commands.PriceManagement.changeProductPriceBuilder().productId(pizza.getId()).price(915).build();
        var sodaPrice = Commands.PriceManagement.changeProductPriceBuilder().productId(soda.getId()).price(115).build();
        priceManagementContext.priceManagementService.changeProductPrice(pizzaPrice);
        priceManagementContext.priceManagementService.changeProductPrice(sodaPrice);

        DocumentId documentId = DocumentId.of("S");
        assert johnDoe.getId() != null;
        Map<String, Object> makeSale = Map.of(
                "documentId", documentId.value(),
                "customerId", johnDoe.getId().value(),
                "products", List.of(
                        Map.of("productId", pizza.getId().value(), "quantity", 1),
                        Map.of("productId", soda.getId().value(), "quantity", 2)
                )
        );
        var jwtToken = jwtToken(username);
        // When
        MvcResult mvcResult = mockMvc.perform(post("/sales/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(makeSale))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(salesContext.saleRepository.findById(TransactionalId.of(documentId, id.longValue())))
                .isPresent()
                .hasValueSatisfying(sale -> {
                    assertThat(sale.getCustomerId()).isEqualTo(johnDoe.getId());
                    assertThat(sale.getTotal()).isEqualTo(MonetaryAmount.of(new BigDecimal("1351.10")));
                    assertThat(sale.getProducts()).hasSize(2);
                    assertThat(sale.getProducts().stream().filter(saleItem -> saleItem.productId().equals(pizza.getId())).findFirst())
                            .isPresent()
                            .hasValueSatisfying(saleItem -> {
                                assertThat(saleItem.productId()).isEqualTo(pizza.getId());
                                assertThat(saleItem.getQuantity()).isEqualTo(Quantity.of(new BigDecimal(1)));
                                assertThat(saleItem.getPrice()).isEqualTo(MonetaryAmount.of(new BigDecimal("915.00")));
                                assertThat(saleItem.getTaxAmount()).isEqualTo(MonetaryAmount.of(new BigDecimal("164.70")));
                                assertThat(saleItem.total()).isEqualTo(MonetaryAmount.of(new BigDecimal("1079.70")));
                                assertThat(saleItem.getTaxes()).hasSize(1);
                                assertThat(saleItem.getTaxes()).anySatisfy(saleProductTax -> {
                                    assertThat(saleProductTax.taxId()).isEqualTo(tax18Percent.getId());
                                    assertThat(saleProductTax.getTaxRate()).isEqualTo(tax18Percent.getRate());
                                    assertThat(saleProductTax.getTaxAmount()).isEqualTo(MonetaryAmount.of(new BigDecimal("164.70")));
                                });
                            });
                    assertThat(sale.getProducts().stream().filter(saleItem -> saleItem.productId().equals(soda.getId())).findFirst())
                            .isPresent()
                            .hasValueSatisfying(saleItem -> {
                                assertThat(saleItem.productId()).isEqualTo(soda.getId());
                                assertThat(saleItem.getQuantity()).isEqualTo(Quantity.of(new BigDecimal(2)));
                                assertThat(saleItem.getPrice()).isEqualTo(MonetaryAmount.of(new BigDecimal("115.00")));
                                assertThat(saleItem.getTaxAmount()).isEqualTo(MonetaryAmount.of(new BigDecimal("20.70")));
                                assertThat(saleItem.total()).isEqualTo(MonetaryAmount.of(new BigDecimal("271.40")));
                                assertThat(saleItem.getTaxes()).hasSize(1);
                                assertThat(saleItem.getTaxes()).anySatisfy(saleProductTax -> {
                                    assertThat(saleProductTax.taxId()).isEqualTo(tax18Percent.getId());
                                    assertThat(saleProductTax.getTaxRate()).isEqualTo(tax18Percent.getRate());
                                    assertThat(saleProductTax.getTaxAmount()).isEqualTo(MonetaryAmount.of(new BigDecimal("20.70")));
                                });
                            });
                });
    }
}
