package org.habilisoft.zemi.invoicing;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.invoicing.usecase.GenerateSaleInvoice;
import org.habilisoft.zemi.invoicing.usecase.GenerateSaleInvoiceUseCase;
import org.habilisoft.zemi.sales.sale.domain.SaleMade;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoicingService {
    private final GenerateSaleInvoiceUseCase generateSaleInvoiceUseCase;
    @ApplicationModuleListener
    void on(SaleMade saleMade) {
        GenerateSaleInvoice generateSaleInvoice = new GenerateSaleInvoice(
                saleMade.transactionalId(),
                saleMade.customerId(),
                saleMade.total(),
                saleMade.time(),
                saleMade.user()
        );
        generateSaleInvoiceUseCase.execute(generateSaleInvoice);
    }
}
