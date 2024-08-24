package org.habilisoft.zemi.sales;

import com.jayway.jsonpath.JsonPath;
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

@DisplayName("Sales Test")
class SalesTest extends AbstractIt {

    @Test
    @DisplayName("Should register a customer")
    void shouldRegisterACustomer() throws Exception {
        // Given
        String johnDoe = "John Doe";
        Map<String, Object> registerJohnDoe = Map.of("name", johnDoe);
        var jwtToken = jwtToken(username);
        // When
        MvcResult mvcResult = mockMvc.perform(post("/sales/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(registerJohnDoe))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        var customerRow = jdbcClient.sql("SELECT * FROM customers WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .single();
        assertThat(customerRow.get("name")).hasToString(johnDoe);
    }
}
