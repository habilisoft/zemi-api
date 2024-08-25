package org.habilisoft.zemi.pricemanagement.pricelist.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceList;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListId;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListNotFound;
import org.habilisoft.zemi.pricemanagement.pricelist.domain.PriceListRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RemoveProductsToPriceListUseCase implements UseCase<RemoveProductsToPriceList, Void> {
    private final PriceListRepository priceListRepository;

    @Override
    public Void execute(RemoveProductsToPriceList removeProductsToPriceList) {
        PriceListId priceListId = removeProductsToPriceList.priceListId();
        PriceList priceList = priceListRepository.findById(priceListId)
                .orElseThrow(() -> new PriceListNotFound(priceListId));
        Username username = Username.of(removeProductsToPriceList.user());
        LocalDateTime time = removeProductsToPriceList.time();
        priceList.removeProduct(removeProductsToPriceList.products(), time, username);
        priceListRepository.save(priceList);
        return null;
    }
}
