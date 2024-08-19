package org.habilisoft.zemi.tenant.infra;

import org.habilisoft.zemi.tenant.TenantId;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER;
import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

@Component
class SchemaAwareConnectionProvider implements MultiTenantConnectionProvider<TenantId>, CurrentTenantIdentifierResolver<TenantId>, HibernatePropertiesCustomizer {
    private final transient DataSource dataSource;
    private final TenantContext tenantContext;
    private final TenantId defaultTenantId = new TenantId("public");

    public SchemaAwareConnectionProvider(DataSource dataSource, TenantContext tenantContext) {
        this.dataSource = new SchemaAwareDatasource(dataSource, tenantContext);
        this.tenantContext = tenantContext;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(TenantId tenantIdentifier) throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseConnection(TenantId tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(@NonNull Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> aClass) {
        throw new UnsupportedOperationException("Can't unwrap this.");
    }

    @Override
    public TenantId resolveCurrentTenantIdentifier() {
        return tenantContext.get().orElse(defaultTenantId);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, this);
        hibernateProperties.put(MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}