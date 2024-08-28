package org.habilisoft.zemi.pricemanagement.customer.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.pricemanagement.customer.domain.CustomerPriceList;
import org.habilisoft.zemi.pricemanagement.customer.domain.CustomerPriceListRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InitializeCustomerPriceListUseCase implements UseCase<InitializeCustomerPriceList, Void> {
    private final CustomerPriceListRepository customerPriceListRepository;
    @Override
    public Void execute(InitializeCustomerPriceList initializeCustomerPriceList) {
        LocalDateTime time = initializeCustomerPriceList.time();
        Username user = Username.of(initializeCustomerPriceList.user());
        CustomerPriceList customerPriceList = CustomerPriceList.initialize(initializeCustomerPriceList.customerId(), time, user);
        customerPriceListRepository.save(customerPriceList);
        return null;
    }
}
