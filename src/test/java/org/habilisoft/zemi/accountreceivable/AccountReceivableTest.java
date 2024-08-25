package org.habilisoft.zemi.accountreceivable;

import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.customer.domain.Customer;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Account Receivable")
class AccountReceivableTest extends AbstractIt {

    @Test
    @DisplayName("Should change the customer credit limit")
    void shouldChangeTheCustomerCreditLimit() throws Exception{
        // Given
        Customer customer = customerFixtures.customer1();
        CustomerId customerId = customer.getId();
        Cookie jwtToken = jwtToken(username);
        Map<String, Object> oneThousand = Map.of(
                "creditLimit", 1000.0
        );
        // When
        assert customerId != null;
        mockMvc.perform(post("/account-receivables/v1/customer/{customerId}/change-credit-limit", customerId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(oneThousand))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(accountReceivableContext.customerArRepository.findById(customerId))
                .isPresent()
                .hasValueSatisfying(customerAr -> {
                    assertThat(customerAr.getCreditLimit()).isEqualTo(MonetaryAmount.of(new BigDecimal("1000.00")));
                });
    }
}
