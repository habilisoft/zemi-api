package org.habilisoft.zemi.catalog;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.AbstractIt;
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
        var productRow = jdbcClient.sql("SELECT * FROM products WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .single();
        assertThat(productRow.get("name")).hasToString(pizza);
        assertThat(productRow.get("created_at")).isNotNull();
        assertThat(productRow.get("created_by")).hasToString(username.value());
    }

    @Test
    @DisplayName("Should update a product")
    void shouldUpdateAProduct() throws Exception {
        // Given
        String pizza = "Pizza";
        Map<String, Object> registerPizza = Map.of("name", pizza);
        Cookie jwtToken = jwtToken(username);
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(registerPizza))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        Integer productId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        String burger = "Burger";
        Map<String, Object> updateBurgerProduct = Map.of("name", burger);
        // When
        mockMvc.perform(post("/catalog/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(updateBurgerProduct))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        var productRow = jdbcClient.sql("SELECT * FROM products WHERE id = :id")
                .param("id", productId)
                .query(rowMapper)
                .single();
        assertThat(productRow.get("name")).hasToString(burger);
        assertThat(productRow.get("updated_at")).isNotNull();
        assertThat(productRow.get("updated_by")).hasToString(username.value());
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
        var categoryRow = jdbcClient.sql("SELECT * FROM categories WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .single();
        assertThat(categoryRow.get("name")).hasToString(food);
        assertThat(categoryRow.get("created_at")).isNotNull();
        assertThat(categoryRow.get("created_by")).hasToString(username.value());
    }

    @Test
    @DisplayName("Should update a category")
    void shouldUpdateACategory() throws Exception {
        // Given
        String food = "Food";
        Map<String, Object> createFoodCategory = Map.of("name", food);
        Cookie jwtToken = jwtToken(username);
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(createFoodCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        Integer categoryId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        String fastFood = "Fast Food";
        Map<String, Object> updateFastFoodCategory = Map.of("name", fastFood);
        // When
        mockMvc.perform(post("/catalog/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(updateFastFoodCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        var categoryRow = jdbcClient.sql("SELECT * FROM categories WHERE id = :id")
                .param("id", categoryId)
                .query(rowMapper)
                .single();
        assertThat(categoryRow.get("name")).hasToString(fastFood);
        assertThat(categoryRow.get("updated_at")).isNotNull();
        assertThat(categoryRow.get("updated_by")).hasToString(username.value());
    }

    @Test
    @DisplayName("Should register a product with a category")
    void shouldRegisterAProductWithACategory() throws Exception {
        // Given
        String food = "Food";
        Map<String, Object> createFoodCategory = Map.of("name", food);
        Cookie jwtToken = jwtToken(username);
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(createFoodCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        Integer categoryId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        String pizza = "Pizza";
        Map<String, Object> registerPizza = Map.of("name", pizza, "categoryId", categoryId);
        // When
        mvcResult = mockMvc.perform(post("/catalog/v1/products")
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
        var productRow = jdbcClient.sql("SELECT * FROM products WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .single();
        assertThat(productRow.get("name")).hasToString(pizza);
        assertThat(productRow).containsEntry("category_id", categoryId);
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