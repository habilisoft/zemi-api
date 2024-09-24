package org.habilisoft.zemi.taxmanagement;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.customer.domain.Customer;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcSeries;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfSequence;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfType;
import org.habilisoft.zemi.taxesmanagement.product.domain.TaxIdAndRate;
import org.habilisoft.zemi.taxesmanagement.tax.domain.Tax;
import org.habilisoft.zemi.taxesmanagement.tax.domain.TaxId;
import org.habilisoft.zemi.util.Commands;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tax Management")
class TaxManagementTest extends AbstractIt {

    @Test
    @DisplayName("Should change the customer ncf type")
    void shouldChangeTheCustomerNcfType() throws Exception {
        // Given
        Customer johnDoe = customerFixtures.customer1();
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

    @Test
    @DisplayName("Should create a tax")
    void shouldCreateATax() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        Map<String, Object> tax = Map.of(
                "name", "ITBIS 18%",
                "rate", 18.0
        );
        // When
        MvcResult mvcResult = mockMvc.perform(post("/tax-management/v1/tax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(tax))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        assertThat(taxManagementContext.taxRepository.findById(TaxId.of(id.longValue())))
                .isPresent()
                .hasValueSatisfying(tax1 -> {
                    assertThat(tax1.getRate().value() * 100).isEqualTo(18.0);
                });
    }

    @Test
    @DisplayName("Should update a tax")
    void shouldUpdateATax() throws Exception {
        // Given
        TaxId taxId = taxManagementFixtures.tax18Percent().getId();
        Cookie jwtToken = jwtToken(username);
        Map<String, Object> tax = Map.of(
                "name", "ITBIS 16%",
                "rate", 16.0
        );
        // When
        assert taxId != null;
        mockMvc.perform(post("/tax-management/v1/tax/{taxId}", taxId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(tax))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(taxManagementContext.taxRepository.findById(taxId))
                .isPresent()
                .hasValueSatisfying(tax16 -> {
                    assertThat(tax16.getRate().value() * 100).isEqualTo(16);
                    assertThat(tax16.getName()).isEqualTo("ITBIS 16%");
                });
    }

    @Test
    @DisplayName("Should add a tax to a product")
    void shouldAddATaxToAProduct() throws Exception {
        // Given
        Tax tax18Percent = taxManagementFixtures.tax18Percent();
        Product product = catalogFixtures.product1();
        ProductId productId = product.getId();
        assert tax18Percent.getId() != null;
        Map<String, Object> productTax = Map.of(
                "taxes", Set.of(tax18Percent.getId().value())
        );
        Cookie jwtToken = jwtToken(username);
        // When
        mockMvc.perform(post("/tax-management/v1/product/{productId}/tax", productId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(productTax))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(taxManagementContext.taxManagementService.getProductTaxes(Set.of(productId)))
                .containsEntry(productId, Set.of(TaxIdAndRate.from(tax18Percent)));
    }

    @Test
    @DisplayName("Should remove a tax from a product")
    void shouldRemoveATaxFromAProduct() throws Exception {
        // Given
        Tax tax18Percent = taxManagementFixtures.tax18Percent();
        Product product = catalogFixtures.product1();
        ProductId productId = product.getId();
        assert tax18Percent.getId() != null;
        taxManagementContext.taxManagementService.addProductTaxes(
                Commands.TaxManagement.addProductTaxesBuilder()
                        .productId(productId)
                        .taxes(Set.of(tax18Percent.getId()))
                        .build()
        );
        Map<String, Object> productTax = Map.of(
                "taxes", Set.of(tax18Percent.getId().value())
        );
        Cookie jwtToken = jwtToken(username);
        // When
        mockMvc.perform(post("/tax-management/v1/product/{productId}/tax/remove", productId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(productTax))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThat(taxManagementContext.taxManagementService.getProductTaxes(Set.of(productId)))
                .doesNotContainKey(productId);
    }

    @Test
    @DisplayName("Should add a ncf sequence")
    void shouldAddANcfSequence() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        Map<String, Object> ncfSequence = Map.of(
                "ncfType", "FISCAL_CREDIT",
                "series", "B",
                "start", 1,
                "end", 100,
                "expirationDate", "2025-12-31"
        );
        // When
        mockMvc.perform(post("/tax-management/v1/ncf-sequence")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(ncfSequence))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // Then
        assertThat(taxManagementContext.ncfSequenceRepository.findByNcfType(NcfType.FISCAL_CREDIT))
                .isPresent()
                .hasValueSatisfying(ncf -> {
                    assertThat(ncf.getSeries()).isEqualTo(NcSeries.B);
                    assertThat(ncf.getInitialSequence()).isEqualTo(1);
                    assertThat(ncf.getFinalSequence()).isEqualTo(100);
                });
    }

    @Test
    @DisplayName("Should not add a ncf sequence with an already active ncf type sequence")
    void shouldNotAddANcfSequenceWithAnAlreadyActiveNcfTypeSequence() throws Exception {
        // Given
        Cookie jwtToken = jwtToken(username);
        NcfSequence existingSequence = taxManagementFixtures.ncfSequenceForFinalConsumer();
        Map<String, Object> ncfSequence = Map.of(
                "ncfType", existingSequence.getNcfType().name(),
                "series", existingSequence.getSeries().name(),
                "start", 1,
                "end", 100,
                "expirationDate", "2025-12-31"
        );
        // When
        mockMvc.perform(post("/tax-management/v1/ncf-sequence")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(tenantHeader, tenant.name())
                        .cookie(jwtToken)
                        .content(serializeRequestToJson(ncfSequence))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
