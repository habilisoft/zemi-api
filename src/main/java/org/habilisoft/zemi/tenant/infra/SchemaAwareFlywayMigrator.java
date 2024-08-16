package org.habilisoft.zemi.tenant.infra;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.habilisoft.zemi.tenant.TenantId;
import org.habilisoft.zemi.tenant.TenantRegistered;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SchemaAwareFlywayMigrator {
    private final DataSource dataSource;

    public void migrate(TenantId... tenants) {
        migrate("classpath:migration/tenant", tenants);
    }

    public void migrate(String location, TenantId... tenants) {
        Arrays.stream(tenants).forEach(tenant -> migrate(location, tenant));
    }

    public void migrate(String location, TenantId tenant) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(location)
                .baselineOnMigrate(true)
                .schemas(tenant.name())
                .load();
        flyway.migrate();
    }

    @Async
    @EventListener
    public void on(TenantRegistered registered) {
        migrate(registered.tenantId());
    }
}
