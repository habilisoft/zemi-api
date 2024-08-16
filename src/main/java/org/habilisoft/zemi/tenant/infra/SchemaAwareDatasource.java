package org.habilisoft.zemi.tenant.infra;

import org.habilisoft.zemi.tenant.TenantId;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

class SchemaAwareDatasource extends DelegatingDataSource {
    private final TenantContext tenantContext;
    private final TenantId defaultTenantId = new TenantId("public");
    public SchemaAwareDatasource(DataSource targetDataSource, TenantContext tenantContext) {
        super(targetDataSource);
        this.tenantContext = tenantContext;
    }
    @NonNull
    @Override
    public Connection getConnection() throws SQLException {
        DataSource dataSource = getTargetDataSource();
        if (dataSource == null) {
            throw new SQLException("No DataSource set");
        }
        Connection connection = dataSource.getConnection();
        connection.setSchema(tenantContext.get().orElse(defaultTenantId).name());
        return connection;
    }

}
