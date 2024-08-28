package org.habilisoft.zemi.pricemanagement;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.customer.domain.Customer;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceList;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListProduct;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.ProductIdAndPrice;
import org.habilisoft.zemi.shared.MonetaryAmount;
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
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@DisplayName("Price Management Test")
class PriceManagementTest extends AbstractIt {
    @Test
    @DisplayName("Should change product price")
    void shouldChangeProductPrice() throws Exception {
        // Given
        Product product = catalogFixtures.product1();
        ProductId productId = product.getId();
        Cookie jwtToken = jwtToken(username);
        Map<String, Object> oneHundred = Map.of(
                "price", 100.0
        );
        // When
        mockMvc.perform(post("/price-management/v1/product/{productId}/change-price", productId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(oneHundred))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(priceManagementContext.priceManagementService.getCurrentPrice(Set.of(productId)))
                .containsEntry(productId, MonetaryAmount.of(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Should create price list")
    void shouldCreatePriceList() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        Product pizza = catalogFixtures.product2();
        ProductId productId = pizza.getId();
        Map<String, Object> priceList = Map.of(
                "name", "Regular",
                "products", Set.of(Map.of(
                        "productId", productId.value(),
                        "price", 100.0
                ))
        );
        // When
        mockMvc.perform(post("/price-management/v1/price-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(priceList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // Then
        assertThat(priceManagementContext.priceListRepository.findAll())
                .hasSize(1)
                .anySatisfy(priceList1 -> {
                    assertThat(priceList1.getName()).isEqualTo("Regular");
                    assertThat(priceList1.getProducts()).hasSize(1);
                    assertThat(priceList1.getProducts())
                            .anySatisfy(priceListProduct -> {
                                assertThat(priceListProduct.getId()).isNotNull()
                                        .satisfies(priceListProductId -> {
                                            assertThat(priceListProductId.productId()).isEqualTo(productId);
                                        });
                                assertThat(priceListProduct.getPrice()).isEqualTo(MonetaryAmount.of(new BigDecimal("100.00")));
                            });
                });
    }

    @Test
    @DisplayName("Should clone price list")
    void shouldClonePriceList() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        PriceList regularPriceList = priceManagementFixtures.priceList1();
        PriceListId priceListId = regularPriceList.getId();
        Map<String, Object> clonePriceList = Map.of(
                "name", "Special"
        );
        // When
        assert priceListId != null;
        MvcResult mvcResult = mockMvc.perform(post("/price-management/v1/price-list/{priceListId}/clone", priceListId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(clonePriceList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(priceManagementContext.priceListRepository.findById(PriceListId.of(id.longValue())))
                .isPresent()
                .hasValueSatisfying(priceList -> {
                    assertThat(priceList.getName()).isEqualTo("Special");
                    assertThat(priceList.getProducts()).hasSize(1);
                    assertThat(priceList.getProducts().stream().map(p -> new ProductIdAndPrice(p.getProductId(), p.getPrice())).toList())
                            .isEqualTo(regularPriceList.getProducts().stream().map(p -> new ProductIdAndPrice(p.getProductId(), p.getPrice())).toList());
                });
    }

    @Test
    @DisplayName("Should add products to price list")
    void shouldAddProductsToPriceList() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        PriceList regularPriceList = priceManagementFixtures.priceList1();
        PriceListId priceListId = regularPriceList.getId();
        Product burger = catalogFixtures.product2();
        ProductId productId = burger.getId();
        assert priceListId != null;
        Map<String, Object> addBurgerToPriceList = Map.of(
                "priceListId", priceListId.value(),
                "products", Set.of(Map.of(
                        "productId", productId.value(),
                        "price", 250
                ))
        );
        // When
        mockMvc.perform(post("/price-management/v1/price-list/{priceListId}/add-products", priceListId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(addBurgerToPriceList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(priceManagementContext.priceListRepository.findById(priceListId))
                .isPresent()
                .hasValueSatisfying(priceList -> {
                    assertThat(priceList.getProducts()).hasSize(2);
                    assertThat(priceList.getProducts())
                            .anySatisfy(priceListProduct -> {
                                assertThat(priceListProduct.getId())
                                        .isNotNull()
                                        .satisfies(priceListProductId -> assertThat(priceListProductId.productId()).isEqualTo(productId));
                                assertThat(priceListProduct.getPrice()).isEqualTo(MonetaryAmount.of(new BigDecimal("250.00")));
                            });
                });
    }

    @Test
    @DisplayName("Should remove products from price list")
    void shouldRemoveProductsFromPriceList() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        PriceList regularPriceList = priceManagementFixtures.priceList2();
        PriceListId priceListId = regularPriceList.getId();
        List<PriceListProduct> products = regularPriceList.getProducts().stream().toList();
        PriceListProduct pizza = products.getFirst();
        PriceListProduct burger = products.getLast();

        assert priceListId != null;
        Map<String, Object> removePizzaFromPriceList = Map.of(
                "products", Set.of(burger.getProductId().value())
        );
        // When
        mockMvc.perform(post("/price-management/v1/price-list/{priceListId}/remove-products", priceListId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(removePizzaFromPriceList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(priceManagementContext.priceListRepository.findById(priceListId))
                .isPresent()
                .hasValueSatisfying(priceList -> {
                    assertThat(priceList.getProducts()).hasSize(2);
                    assertThat(priceList.getProducts())
                            .anySatisfy(priceListProduct -> {
                                assertThat(priceListProduct.getId())
                                        .isNotNull()
                                        .satisfies(priceListProductId -> assertThat(priceListProductId.productId()).isEqualTo(pizza.getProductId()));
                                assertThat(priceListProduct.getPrice()).isEqualTo(pizza.getPrice());
                                assertThat(priceListProduct.isCurrent()).isTrue();
                            })
                            .anySatisfy(priceListProduct -> {
                                assertThat(priceListProduct.getId())
                                        .isNotNull()
                                        .satisfies(priceListProductId -> assertThat(priceListProductId.productId()).isEqualTo(burger.getProductId()));
                                assertThat(priceListProduct.getPrice()).isEqualTo(burger.getPrice());
                                assertThat(priceListProduct.isCurrent()).isFalse();
                            });
                });
    }

    @Test
    @DisplayName("Should change product current price")
    void shouldChangeProductCurrentPrice() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        PriceList regularPriceList = priceManagementFixtures.priceList1();
        PriceListId priceListId = regularPriceList.getId();
        Set<PriceListProduct> products = regularPriceList.getProducts();
        PriceListProduct pizza = products.iterator().next();
        Map<String, Object> changePizzaPrice = Map.of(
                "products", Set.of(Map.of(
                        "productId", pizza.getProductId().value(),
                        "price", 1000
                ))
        );

        // When
        assert priceListId != null;
        mockMvc.perform(post("/price-management/v1/price-list/{priceListId}/change-products-price", priceListId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(changePizzaPrice))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(priceManagementContext.priceListRepository.findById(priceListId))
                .isPresent()
                .hasValueSatisfying(priceList -> {
                    assertThat(priceList.getProducts()).hasSize(2);
                    assertThat(priceList.getProducts())
                            .anySatisfy(priceListProduct -> {
                                assertThat(priceListProduct.getId())
                                        .isNotNull()
                                        .satisfies(priceListProductId -> assertThat(priceListProductId.productId()).isEqualTo(pizza.getProductId()));
                                assertThat(priceListProduct.getPrice()).isEqualTo(pizza.getPrice());
                                assertThat(priceListProduct.isCurrent()).isFalse();
                            })
                            .anySatisfy(priceListProduct -> {
                                assertThat(priceListProduct.getId())
                                        .isNotNull()
                                        .satisfies(priceListProductId -> assertThat(priceListProductId.productId()).isEqualTo(pizza.getProductId()));
                                assertThat(priceListProduct.getPrice()).isEqualTo(MonetaryAmount.of(new BigDecimal("1000.00")));
                                assertThat(priceListProduct.isCurrent()).isTrue();
                            });
                });
    }

    @Test
    @DisplayName("Should update price list")
    void shouldUpdatePriceList() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        PriceList regularPriceList = priceManagementFixtures.priceList1();
        PriceListId priceListId = regularPriceList.getId();
        Map<String, Object> updatePriceList = Map.of(
                "name", "Special"
        );
        // When
        assert priceListId != null;
        mockMvc.perform(post("/price-management/v1/price-list/{priceListId}/update", priceListId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(updatePriceList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(priceManagementContext.priceListRepository.findById(priceListId))
                .isPresent()
                .hasValueSatisfying(priceList -> {
                    assertThat(priceList.getName()).isEqualTo("Special");
                });
    }

    @Test
    @DisplayName("Should change customer price list")
    void shouldChangeCustomerPriceList() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        PriceList regularPriceList = priceManagementFixtures.priceList1();
        PriceListId priceListId = regularPriceList.getId();
        Customer johnDoe = customerFixtures.customer1();
        CustomerId customerId = johnDoe.getId();
        assert priceListId != null;
        Map<String, Object> changeCustomerPriceList = Map.of(
                "priceListId", priceListId.value()
        );
        // When
        assert customerId != null;
        await().untilAsserted(() -> assertThat(priceManagementContext.customerPriceListRepository.findById(customerId)).isPresent());
        mockMvc.perform(post("/price-management/v1/customer/{customerId}/change-price-list", customerId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(changeCustomerPriceList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(priceManagementContext.customerPriceListRepository.findById(customerId))
                .isPresent()
                .hasValueSatisfying(customerPriceList -> {
                    assertThat(customerPriceList.getPriceListId()).isEqualTo(priceListId);
                });
    }
}

