package org.habilisoft.zemi.pricemanagement.pricelist.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceListIdGenerator {
    private final JdbcTemplate jdbcTemplate;

    public PriceListId generate() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('price_lists', 'id'));", Long.class);
        return PriceListId.of(sequence);
    }
}
