package org.habilisoft.zemi.invoicing.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.invoicing.domain.Invoice;
import org.habilisoft.zemi.invoicing.domain.InvoiceRepository;
import org.habilisoft.zemi.shared.IdempotentUseCase;
import org.habilisoft.zemi.taxesmanagement.TaxManagementService;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.Ncf;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateSaleInvoiceUseCase implements IdempotentUseCase<GenerateSaleInvoice, Void> {
    private final InvoiceRepository invoiceRepository;
    private final TaxManagementService taxManagementService;

    @Override
    public String idempotencyKey(GenerateSaleInvoice generateSaleInvoice) {
        return generateSaleInvoice.transactionalId().toString();
    }

    @Override
    public Void execute(GenerateSaleInvoice generateSaleInvoice) {
        Ncf ncf = taxManagementService.generateNcfForCustomer(
                generateSaleInvoice.customerId(),
                generateSaleInvoice.user(),
                generateSaleInvoice.time()
        );
        Invoice invoice = Invoice.generate(
                generateSaleInvoice.transactionalId(),
                generateSaleInvoice.customerId(),
                ncf,
                generateSaleInvoice.total(),
                generateSaleInvoice.time(),
                Username.of(generateSaleInvoice.user())
        );
        invoiceRepository.save(invoice);
        return null;
    }
}
