package org.habilisoft.zemi.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateCategoryIdGenerator {
    private final JdbcTemplate jdbcTemplate;

    public CategoryId generate() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('categories', 'id'));", Long.class);
        return CategoryId.of(sequence);
    }
}
