package org.habilisoft.zemi.catalog.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.CatalogService;
import org.habilisoft.zemi.catalog.category.application.CreateCategory;
import org.habilisoft.zemi.catalog.category.application.UpdateCategory;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.product.application.RegisterProduct;
import org.habilisoft.zemi.catalog.product.application.UpdateProduct;
import org.habilisoft.zemi.catalog.product.domain.ProductId;
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
                request.isService(),
                LocalDateTime.now(),
                userService.getCurrentUser()
        );
        ProductId productId = catalogService.registerProduct(registerProduct);
        return Map.of("id", productId.value());
    }

    @PostMapping("products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateProduct(@PathVariable Long productId, @RequestBody @Valid Requests.UpdateProduct request) {
        catalogService.updateProduct(new UpdateProduct(
                ProductId.of(productId),
                request.getCategoryId(),
                request.getName(),
                request.getIsService(),
                LocalDateTime.now(),
                userService.getCurrentUser()
        ));
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

    @PostMapping("categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateCategory(@PathVariable Long categoryId, @RequestBody @Valid Requests.UpdateCategory request) {
        catalogService.updateCategory(new UpdateCategory(
                CategoryId.of(categoryId),
                request.name(),
                LocalDateTime.now(),
                userService.getCurrentUser()
        ));
    }
}
