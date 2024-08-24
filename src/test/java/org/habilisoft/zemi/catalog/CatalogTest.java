package org.habilisoft.zemi.catalog;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.catalog.category.domain.Category;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Catalog Test")
class CatalogTest extends AbstractIt {
    @Test
    @DisplayName("Should register a product")
    void shouldRegisterAProduct() throws Exception {
        // Given
        String pizza = "Pizza";
        Map<String, Object> registerPizza = Map.of("name", pizza);
        // When
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken(username))
                        .content(serializeRequestToJson(registerPizza))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(catalogContext.productRepository.findById(ProductId.of(id.longValue())))
                .isPresent()
                .hasValueSatisfying(product -> {
                    assertThat(product.getName()).isEqualTo(pizza);
                    assertThat(product.getAuditableProperties().createdBy()).isEqualTo(username);
                    assertThat(product.getAuditableProperties().createdAt()).isNotNull();
                });
    }

    @Test
    @DisplayName("Should update a product")
    void shouldUpdateAProduct() throws Exception {
        // Given
        Product pizza = catalogFixtures.product1();
        ProductId productId = pizza.getId();
        String burger = "Burger";
        Map<String, Object> updateBurgerProduct = Map.of("name", burger);
        Cookie jwtToken = jwtToken(username);
        // When
        mockMvc.perform(post("/catalog/v1/products/{id}", productId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(updateBurgerProduct))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(catalogContext.productRepository.findById(pizza.getId()))
                .isPresent()
                .hasValueSatisfying(product -> {
                    assertThat(product.getName()).isEqualTo(burger);
                    assertThat(product.getAuditableProperties().updatedBy()).isEqualTo(username);
                    assertThat(product.getAuditableProperties().updatedAt()).isNotNull();
                });
    }

    @Test
    @DisplayName("Should create a category")
    void shouldCreateACategory() throws Exception {
        // Given
        String food = "Food";
        Map<String, Object> createFoodCategory = Map.of("name", food);
        // When
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken(username))
                        .content(serializeRequestToJson(createFoodCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(catalogContext.categoryRepository.findById(CategoryId.of(id.longValue())))
                .isPresent()
                .hasValueSatisfying(category -> {
                    assertThat(category.getName()).isEqualTo(food);
                    assertThat(category.getAuditableProperties().createdBy()).isEqualTo(username);
                    assertThat(category.getAuditableProperties().createdAt()).isNotNull();
                });
    }

    @Test
    @DisplayName("Should update a category")
    void shouldUpdateACategory() throws Exception {
        // Given
        Category food = catalogFixtures.category1();
        Long categoryId = food.getId().value();
        String fastFood = "Fast Food";
        Map<String, Object> updateFastFoodCategory = Map.of("name", fastFood);
        Cookie jwtToken = jwtToken(username);
        // When
        mockMvc.perform(post("/catalog/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(updateFastFoodCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(catalogContext.categoryRepository.findById(food.getId()))
                .isPresent()
                .hasValueSatisfying(category -> {
                    assertThat(category.getName()).isEqualTo(fastFood);
                    assertThat(category.getAuditableProperties().updatedBy()).isEqualTo(username);
                    assertThat(category.getAuditableProperties().updatedAt()).isNotNull();
                });
    }

    @Test
    @DisplayName("Should register a product with a category")
    void shouldRegisterAProductWithACategory() throws Exception {
        // Given
        Category category = catalogFixtures.category1();
        String pizza = "Pizza";
        Map<String, Object> registerPizza = Map.of("name", pizza, "categoryId", category.getId());
        Cookie jwtToken = jwtToken(username);
        // When
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(registerPizza))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(catalogContext.productRepository.findById(ProductId.of(id.longValue()))
                .orElseThrow())
                .extracting("name", "categoryId")
                .containsExactly(pizza, category.getId());
    }

    @Test
    @DisplayName("Should not register a product with a non-existent category")
    void shouldNotRegisterAProductWithANonExistentCategory() throws Exception {
        // Given
        String pizza = "Pizza";
        Map<String, Object> registerPizza = Map.of("name", pizza, "categoryId", 1);
        // When
        mockMvc.perform(post("/catalog/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken(username))
                        .content(serializeRequestToJson(registerPizza))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

}