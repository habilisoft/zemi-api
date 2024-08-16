package org.habilisoft.zemi.tenant.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class SchemaAwareJdbc {
    private final DataSource dataSource;
    private final TenantContext tenantContext;

    @Bean
    JdbcTemplate jdbcTemplate() {
        DataSource schemaAwareDatasource = new SchemaAwareDatasource(dataSource, tenantContext);
        return new JdbcTemplate(schemaAwareDatasource);
    }
    @Bean
    JdbcClient jdbcClient() {
        DataSource schemaAwareDatasource = new SchemaAwareDatasource(dataSource, tenantContext);
        return JdbcClient.create(schemaAwareDatasource);
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        DataSource schemaAwareDatasource = new SchemaAwareDatasource(dataSource, tenantContext);
        return new NamedParameterJdbcTemplate(schemaAwareDatasource);
    }
}
