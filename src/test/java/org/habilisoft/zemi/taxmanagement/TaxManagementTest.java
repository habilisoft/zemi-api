package org.habilisoft.zemi.taxmanagement;

import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.sales.customer.domain.Customer;
import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.habilisoft.zemi.taxesmanagement.domain.NcfType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tax Management")
class TaxManagementTest extends AbstractIt {

    @Test
    @DisplayName("Should change the customer ncf type")
    void shouldChangeTheCustomerNcfType() throws Exception {
        // Given
        Customer johnDoe = salesFixtures.customer1();
        CustomerId customerId = johnDoe.getId();
        Cookie jwtToken = jwtToken(username);
        Map<String, Object> fiscalCredit = Map.of(
                "ncfType", NcfType.FISCAL_CREDIT
        );
        // When
        assert customerId != null;
        mockMvc.perform(post("/tax-management/v1/customer/{customerId}/change-ncf-type", customerId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(fiscalCredit))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(taxManagementContext.customerTaxRepository.findById(customerId))
                .isPresent()
                .hasValueSatisfying(customerTax -> {
                    assertThat(customerTax.getNcfType()).isEqualTo(NcfType.FISCAL_CREDIT);
                });
    }
}
