package org.habilisoft.zemi.tenant.infra;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
class SchemaAwareConnectionProvider implements ConnectionProvider {
    private final transient DataSource dataSource;

    public SchemaAwareConnectionProvider(DataSource dataSource, TenantContext tenantContext) {
        this.dataSource = new SchemaAwareDatasource(dataSource, tenantContext);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        dataSource.getConnection().close();
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
}