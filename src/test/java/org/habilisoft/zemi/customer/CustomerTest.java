package org.habilisoft.zemi.customer;

import com.jayway.jsonpath.JsonPath;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.customer.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@DisplayName("Customer Test")
class CustomerTest extends AbstractIt {
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
        MvcResult mvcResult = mockMvc.perform(post("/customers/v1")
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
        CustomerId customerId = CustomerId.of(id.longValue());
        assertThat(customerContext.customerRepository.findById(customerId))
                .isPresent()
                .hasValueSatisfying(customer -> {
                    assertThat(customer.getName()).isEqualTo(johnDoe);
                    assertThat(customer.getType()).isEqualTo(CustomerType.PERSON);
                    assertThat(customer.getContact()).isEqualTo(
                            Contact.of(
                                    Stream.of("809-555-5555", "809-555-5556").map(PhoneNumber::of).collect(Collectors.toSet()),
                                    EmailAddress.of("jhon@zemi.com"))
                    );
                    assertThat(customer.getAddress()).isEqualTo(
                            Set.of(Address.of("Calle 1", "Santo Domingo", "10101"))
                    );
                });

        await().untilAsserted(() ->
                assertThat(taxManagementContext.customerTaxRepository.findById(customerId))
                        .isPresent()
        );
        await().untilAsserted(() ->
                assertThat(accountReceivableContext.customerArRepository.findById(customerId))
                        .isPresent()
        );
    }
}
