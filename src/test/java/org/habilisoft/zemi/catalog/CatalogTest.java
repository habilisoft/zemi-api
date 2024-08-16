package org.habilisoft.zemi.catalog;

import com.jayway.jsonpath.JsonPath;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.catalog.api.CreateCategoryRequest;
import org.habilisoft.zemi.catalog.api.RegisterProductRequest;
import org.habilisoft.zemi.util.Requests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Product Service Test")
class CatalogTest extends AbstractIt {
    @Test
    @DisplayName("Should register a product")
    void shouldRegisterAProduct() throws Exception {
        // Given
        String pizza = "Pizza";
        RegisterProductRequest registerPizza = Requests.Catalog.registerProductBuilder()
                .name(pizza)
                .build();
        // When
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken())
                        .content(serializeRequestToJson(registerPizza))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").isNotEmpty())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.value");
        var productRow = jdbcClient.sql("SELECT * FROM products WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .single();
        assertThat(productRow.get("name")).hasToString(pizza);
    }

    @Test
    @DisplayName("Should create a category")
    void shouldCreateACategory() throws Exception {
        // Given
        String food = "Food";
        CreateCategoryRequest createFoodCategory = Requests.Catalog.createCategoryBuilder()
                .name(food)
                .build();
        // When
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken())
                        .content(serializeRequestToJson(createFoodCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").isNotEmpty())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.value");
        var categoryRow = jdbcClient.sql("SELECT * FROM categories WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .single();
        assertThat(categoryRow.get("name")).hasToString(food);
    }

    @Test
    @DisplayName("Should register a product with a category")
    void shouldRegisterAProductWithACategory() throws Exception {
        // Given
        String food = "Food";
        CreateCategoryRequest createFoodCategory = Requests.Catalog.createCategoryBuilder()
                .name(food)
                .build();
        MvcResult mvcResult = mockMvc.perform(post("/catalog/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken())
                        .content(serializeRequestToJson(createFoodCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").isNotEmpty())
                .andReturn();
        Integer categoryId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.value");
        String pizza = "Pizza";
        RegisterProductRequest registerPizza = Requests.Catalog.registerProductBuilder()
                .name(pizza)
                .categoryId(categoryId)
                .build();
        // When
        mvcResult = mockMvc.perform(post("/catalog/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken())
                        .content(serializeRequestToJson(registerPizza))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").isNotEmpty())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.value");
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
        RegisterProductRequest registerPizza = Requests.Catalog.registerProductBuilder()
                .name(pizza)
                .categoryId(999)
                .build();
        // When
        mockMvc.perform(post("/catalog/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken())
                        .content(serializeRequestToJson(registerPizza))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

}