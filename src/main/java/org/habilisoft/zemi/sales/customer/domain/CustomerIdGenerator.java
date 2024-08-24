package org.habilisoft.zemi.sales.customer.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerIdGenerator {
    private final JdbcTemplate jdbcTemplate;

    public CustomerId generate() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('business_entities', 'id'));", Long.class);
        return CustomerId.of(sequence);
    }
}
