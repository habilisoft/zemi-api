package org.habilisoft.zemi.sales;

import com.jayway.jsonpath.JsonPath;
import org.habilisoft.zemi.AbstractIt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
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
        Map<String, Object> registerJohnDoe = Map.of(
                "name", johnDoe,
                "type", "PERSON",
                "emailAddress", "jhon@zemi.com",
                "address", Map.of(
                        "street", "Calle 1",
                        "city", "Santo Domingo",
                        "zipCode", "10101"
                ),
                "phoneNumbers", List.of("809-555-5555", "809-555-5556")
        );
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
        var customerRow = jdbcClient.sql("""
                        SELECT * 
                        FROM customers c
                        join business_entity_addresses bea on c.id = bea.business_entity_id
                        join business_entity_phones bep on c.id = bep.business_entity_id
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(rowMapper)
                .list();
        assertThat(customerRow).first().extracting("name").isEqualTo(johnDoe);
        assertThat(customerRow).first().extracting("type").isEqualTo("PERSON");
        assertThat(customerRow).first().extracting("email").isEqualTo("jhon@zemi.com");
        assertThat(customerRow).first().extracting("street").isEqualTo("Calle 1");
        assertThat(customerRow).first().extracting("city").isEqualTo("Santo Domingo");
        assertThat(customerRow).first().extracting("zip_code").isEqualTo("10101");
        assertThat(customerRow).extracting("phone").contains("809-555-5555", "809-555-5556");
    }
}
