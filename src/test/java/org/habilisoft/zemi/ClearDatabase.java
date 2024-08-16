package org.habilisoft.zemi;

import org.flywaydb.core.Flyway;
import org.habilisoft.zemi.tenant.TenantId;
import org.habilisoft.zemi.tenant.TenantService;
import org.habilisoft.zemi.tenant.infra.SchemaAwareFlywayMigrator;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Set;

public class ClearDatabase implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext extensionContext) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);
        TenantService tenantService = applicationContext.getBean(TenantService.class);
        SchemaAwareFlywayMigrator schemaAwareFlywayMigrator = applicationContext.getBean(SchemaAwareFlywayMigrator.class);
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        Set<TenantId> tenants = tenantService.allTenants();
        tenants.forEach(tenantId -> clean(dataSource, tenantId));
        schemaAwareFlywayMigrator.migrate("classpath:migration/tenant", tenants.toArray(TenantId[]::new));

    }

    private void clean(DataSource dataSource, TenantId... tenants) {
        Arrays.stream(tenants).forEach(tenant -> {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(tenant.name())
                    .cleanDisabled(false)
                    .load();
            flyway.clean();
        });
    }
}
