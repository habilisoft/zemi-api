package org.habilisoft.zemi.catalog.category.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryIdGenerator {
    private final JdbcTemplate jdbcTemplate;

    public CategoryId generate() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('categories', 'id'));", Long.class);
        return CategoryId.of(sequence);
    }
}
