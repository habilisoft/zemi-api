package org.habilisoft.zemi.query;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller("catalogQuery")
public class CatalogController {
    private final JdbcClient jdbcClient;

    @QueryMapping
    public List<Product> products() {
        return jdbcClient.sql("SELECT * FROM products")
                .query(Product.class)
                .list();
    }

    @QueryMapping
    public Optional<Product> product(@Argument Long id) {
        return jdbcClient.sql("SELECT * FROM products WHERE id = :id")
                .param("id", id)
                .query(Product.class)
                .optional();
    }

    @SchemaMapping
    public Optional<Category> category(@NonNull Product product) {
        return Optional.ofNullable(product.categoryId())
                .flatMap(categoryId -> jdbcClient.sql("SELECT * FROM categories WHERE id = :id")
                        .param("id", categoryId)
                        .query(Category.class)
                        .optional());
    }

    @QueryMapping
    public List<Category> categories() {
        return jdbcClient.sql("SELECT * FROM categories")
                .query(Category.class)
                .list();
    }

    @QueryMapping
    public List<Product> category(@Argument Long id) {
        return jdbcClient.sql("SELECT * FROM products WHERE category_id = :categoryId")
                .param("categoryId", id)
                .query(Product.class)
                .list();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return jdbcClient.sql("SELECT * FROM products WHERE category_id = :categoryId")
                .param("categoryId", categoryId)
                .query(Product.class)
                .list();
    }

}
