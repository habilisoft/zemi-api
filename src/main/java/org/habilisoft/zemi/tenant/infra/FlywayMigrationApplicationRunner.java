package org.habilisoft.zemi.tenant.infra;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.tenant.TenantService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class FlywayMigrationApplicationRunner implements ApplicationRunner {
    private final TenantService tenantService;
    private final SchemaAwareFlywayMigrator schemaAwareFlywayMigrator;
    @Override
    public void run(ApplicationArguments args) {
        tenantService.allTenants()
                .forEach(schemaAwareFlywayMigrator::migrate);
    }
}
