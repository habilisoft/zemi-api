package org.habilisoft.zemi.pricemanagement.pricelist.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceList;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListNotFound;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdatePriceListUseCase implements UseCase<UpdatePriceList, Void> {
    private final PriceListRepository priceListRepository;

    @Override
    public Void execute(UpdatePriceList updatePriceList) {
        PriceList priceList = priceListRepository.findById(updatePriceList.priceListId())
                .orElseThrow(() -> new PriceListNotFound(updatePriceList.priceListId()));
        LocalDateTime time = updatePriceList.time();
        Username username = Username.of(updatePriceList.user());
        priceList.update(updatePriceList.name(), time, username);
        priceListRepository.save(priceList);
        return null;
    }
}
