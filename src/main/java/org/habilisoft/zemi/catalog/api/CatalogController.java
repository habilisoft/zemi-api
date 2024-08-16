package org.habilisoft.zemi.catalog.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.catalog.*;
import org.habilisoft.zemi.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalog/v1")
class CatalogController {
    private final UserService userService;
    private final CatalogService catalogService;

    @PostMapping("products")
    @ResponseStatus(HttpStatus.CREATED)
    ProductId createProduct(@RequestBody @Valid RegisterProductRequest request) {
        RegisterProduct registerProduct = new RegisterProduct(
                Optional.ofNullable(request.categoryId()).map(CategoryId::of),
                request.name(),
                LocalDateTime.now(),
                userService.getCurrentUser()
        );
        return catalogService.registerProduct(registerProduct);
    }

    @PostMapping("categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryId createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        CreateCategory createCategory = new CreateCategory(
                request.name(),
                LocalDateTime.now(),
                userService.getCurrentUser()
        );
        return catalogService.createCategory(createCategory);
    }
}
