package org.habilisoft.zemi.tenant;

import org.springframework.data.repository.RepositoryDefinition;

import java.util.Set;

@RepositoryDefinition(domainClass = Tenant.class, idClass = TenantId.class)
interface TenantRepository {
    Tenant save(Tenant tenant);
    Set<Tenant> findAll();
}
