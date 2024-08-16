package org.habilisoft.zemi.tenant;

import java.io.Serializable;

public record TenantId(String name) implements Serializable {
    public static TenantId of(String name) {
        return new TenantId(name);
    }
}
