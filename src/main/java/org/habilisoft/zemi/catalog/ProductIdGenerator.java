package org.habilisoft.zemi.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ProductIdGenerator {
    private final JdbcTemplate jdbcTemplate;

    public ProductId generate() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('products', 'id'));", Long.class);
        return ProductId.of(sequence);
    }
}