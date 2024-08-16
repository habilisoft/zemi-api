package org.habilisoft.zemi.user.domain;

import java.io.Serializable;

public record PermissionName(String value) implements Serializable {
    public static PermissionName from(String value) {
        return new PermissionName(value);
    }
}
