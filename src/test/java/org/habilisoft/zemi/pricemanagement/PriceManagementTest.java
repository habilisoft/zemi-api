package org.habilisoft.zemi.pricemanagement;

import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Price Management Test")
class PriceManagementTest extends AbstractIt {
    @Test
    @DisplayName("Should change product price")
    void shouldChangeProductPrice() throws Exception{
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
}

