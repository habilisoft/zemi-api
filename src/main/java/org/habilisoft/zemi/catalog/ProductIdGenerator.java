package org.habilisoft.zemi.catalog;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.IdGenerator;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
class ProductIdGenerator implements IdGenerator {
    private final JdbcTemplate jdbcTemplate;

    public ProductId generate() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('products', 'id'));", Long.class);
        return ProductId.of(sequence);
    }
}
