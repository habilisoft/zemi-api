package org.habilisoft.zemi.tenant;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.AbstractAggregateRoot;

@Data
@Entity
@Table(name = "tenants", schema = "public")
@EqualsAndHashCode(of = "id", callSuper = false)
class Tenant extends AbstractAggregateRoot<Tenant> {
    @EmbeddedId
    private TenantId id;
    static Tenant register(TenantId id) {
        var tenant = new Tenant();
        tenant.id = id;
        tenant.registerEvent(new TenantRegistered(id));
        return tenant;
    }
}
