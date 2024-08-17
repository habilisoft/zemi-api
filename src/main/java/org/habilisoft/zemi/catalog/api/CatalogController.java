package org.habilisoft.zemi.catalog.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.*;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalog/v1")
class CatalogController {
    private final UserService userService;
    private final CatalogService catalogService;

    @PostMapping("products")
    @ResponseStatus(HttpStatus.CREATED)
    Map<String, Object> createProduct(@RequestBody @Valid Requests.RegisterProduct request) {
        RegisterProduct registerProduct = new RegisterProduct(
                Optional.ofNullable(request.categoryId()),
                request.name(),
                LocalDateTime.now(),
                userService.getCurrentUser()
        );
        ProductId productId = catalogService.registerProduct(registerProduct);
        return Map.of("id", productId.value());
    }

    @PostMapping("categories")
    @ResponseStatus(HttpStatus.CREATED)
    Map<String, Object> createCategory(@RequestBody @Valid Requests.CreateCategory request) {
        CreateCategory createCategory = new CreateCategory(
                request.name(),
                LocalDateTime.now(),
                userService.getCurrentUser()
        );
        CategoryId categoryId = catalogService.createCategory(createCategory);
        return Map.of("id", categoryId.value());
    }
}
