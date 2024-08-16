package org.habilisoft.zemi.tenant.infra;

import org.habilisoft.zemi.tenant.TenantId;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;
@Component
public class TenantContext implements Serializable {
    private final transient ThreadLocal<TenantId> currentTenant = new InheritableThreadLocal<>();

    public Optional<TenantId> get() {
        return Optional.ofNullable(currentTenant.get());
    }

    public void set(TenantId tenant) {
        this.currentTenant.set(tenant);
    }

    public void clear() {
        this.currentTenant.remove();
    }
}
