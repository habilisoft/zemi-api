package org.habilisoft.zemi.sales.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.customer.domain.CustomerId;
import org.habilisoft.zemi.sales.SalesService;
import org.habilisoft.zemi.sales.sale.application.MakeSale;
import org.habilisoft.zemi.shared.DocumentId;
import org.habilisoft.zemi.shared.Quantity;
import org.habilisoft.zemi.shared.TransactionalId;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("sales/v1")
public class SalesController {
    private final SalesService salesService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> makeSale(@RequestBody @Valid Requests.MakeSale makeSale) {
        TransactionalId transactionalId = salesService.makeSale(new MakeSale(DocumentId.of(makeSale.documentId()), Optional.ofNullable(makeSale.customerId()).map(CustomerId::of), makeSale.products().stream().map(item -> new MakeSale.Product(ProductId.of(item.productId()), Quantity.of(item.quantity()))).collect(Collectors.toSet()), LocalDateTime.now(), userService.getCurrentUser()));
        return Map.of("id", transactionalId.sequence());
    }
}
