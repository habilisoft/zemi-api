package org.habilisoft.zemi.pricemanagement.pricelist.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.*;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClonePriceListUseCase implements UseCase<ClonePriceList, PriceListId> {
    private final PriceListIdGenerator priceListIdGenerator;
    private final PriceListRepository priceListRepository;

    @Override
    public PriceListId execute(ClonePriceList clonePriceList) {
        PriceListId priceListId = clonePriceList.originPriceList();
        PriceList priceList = priceListRepository.findById(priceListId)
                .orElseThrow(() -> new PriceListNotFound(priceListId));
        PriceListId newPriceListId = priceListIdGenerator.generate();
        Username username = Username.of(clonePriceList.user());
        LocalDateTime time = clonePriceList.time();
        PriceList cloned = priceList.clone(newPriceListId, clonePriceList.name(), time, username);
        priceListRepository.save(cloned);
        return newPriceListId;
    }
}
