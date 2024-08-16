package org.habilisoft.zemi.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {
    private final TenantRepository repository;
    public void register(TenantId tenantId) {
        Tenant tenant = Tenant.register(tenantId);
        repository.save(tenant);
    }

    public Set<TenantId> allTenants() {
        return repository.findAll().stream()
                .map(Tenant::getId)
                .collect(Collectors.toSet());
    }
}
