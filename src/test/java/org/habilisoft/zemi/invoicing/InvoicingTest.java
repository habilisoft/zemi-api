package org.habilisoft.zemi.invoicing;

import org.habilisoft.zemi.AbstractIt;
import org.habilisoft.zemi.customer.domain.Customer;
import org.habilisoft.zemi.sales.sale.domain.SaleMade;
import org.habilisoft.zemi.shared.DocumentId;
import org.habilisoft.zemi.shared.MonetaryAmount;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.taxesmanagement.application.ChangeCustomerNcfType;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.Ncf;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfSequence;
import org.habilisoft.zemi.util.Commands;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.Scenario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.habilisoft.zemi.util.Commands.user;

@DisplayName("Invoicing")
class InvoicingTest extends AbstractIt {
    @Test
    @DisplayName("should generate invoice for sales")
    void shouldGenerateInvoiceForSales(Scenario scenario) {
        // Given
        Customer customer = customerFixtures.customer1();
        NcfSequence ncfSequence = taxManagementFixtures.ncfSequenceForFinalConsumer();
        ChangeCustomerNcfType changeCustomerNcfType = Commands.TaxManagement.changeCustomerNcfTypeBuilder()
                .customerId(customer.getId())
                .ncfType(ncfSequence.getNcfType())
                .build();
        taxManagementContext.taxManagementService.changeCustomerNcfType(changeCustomerNcfType);
        TransactionalId transactionalId = transactionalIdGenerator.generate(DocumentId.of("SALE"));
        SaleMade saleMade = new SaleMade(
                transactionalId,
                customer.getId(),
                new MonetaryAmount(new BigDecimal("100.00")),
                LocalDateTime.now(), user
        );
        // When
        scenario.publish(saleMade).andWaitForStateChange(() -> invoicingContext.invoiceRepository.findById(transactionalId))
                .andVerify(optionalInvoice -> assertThat(optionalInvoice).isPresent().hasValueSatisfying(invoice -> {
                    assertThat(invoice.getCustomerId()).isEqualTo(customer.getId());
                    assertThat(invoice.getTotal()).isEqualTo(new MonetaryAmount(new BigDecimal("100.00")));
                    assertThat(taxManagementContext.ncfSequenceRepository.findByNcfType(ncfSequence.getNcfType()))
                            .hasValueSatisfying(sequence -> {
                                Ncf ncf = invoice.getNcf();
                                assertThat(ncf.ncfType()).isEqualTo(sequence.getNcfType());
                                assertThat(Long.parseLong(ncf.sequence())).isEqualTo(sequence.getCurrentSequence() - 1L);
                                assertThat(ncf.series()).isEqualTo(sequence.getSeries());
                            });
                }));
    }
}
