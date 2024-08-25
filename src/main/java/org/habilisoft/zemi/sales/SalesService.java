package org.habilisoft.zemi.sales;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.sales.sale.application.MakeSale;
import org.habilisoft.zemi.sales.sale.application.MakeSaleUseCase;
import org.habilisoft.zemi.shared.TransactionalId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final MakeSaleUseCase makeSaleUseCase;
    public TransactionalId makeSale(MakeSale makeSale) {
        return makeSaleUseCase.execute(makeSale);
    }

}
