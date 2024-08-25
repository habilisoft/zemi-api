package org.habilisoft.zemi.taxesmanagement.tax.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxIdGenerator {
    private final JdbcTemplate jdbcTemplate;

    public TaxId generate() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('taxes', 'id'));", Long.class);
        return TaxId.of(sequence);
    }
}
