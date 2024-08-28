package org.habilisoft.zemi.pricemanagement.customer.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.customer.domain.CustomerNotFound;
import org.habilisoft.zemi.pricemanagement.customer.domain.CustomerPriceList;
import org.habilisoft.zemi.pricemanagement.customer.domain.CustomerPriceListRepository;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListNotFound;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChangeCustomerPriceListUseCase implements UseCase<ChangeCustomerPriceList,Void> {
    private final PriceListRepository priceListRepository;
    private final CustomerPriceListRepository customerPriceListRepository;
    @Override
    public Void execute(ChangeCustomerPriceList changeCustomerPriceList) {
        PriceListId priceListId = changeCustomerPriceList.priceListId();
        if (!priceListRepository.existsById(priceListId)) {
            throw new PriceListNotFound(priceListId);
        }
        CustomerPriceList customerPriceList = customerPriceListRepository.findById(changeCustomerPriceList.customerId())
                .orElseThrow(() -> new CustomerNotFound(changeCustomerPriceList.customerId()));
        LocalDateTime time = changeCustomerPriceList.time();
        Username user = Username.of(changeCustomerPriceList.user());
        customerPriceList.changePriceList(priceListId, time, user);
        customerPriceListRepository.save(customerPriceList);
        return null;
    }
}
