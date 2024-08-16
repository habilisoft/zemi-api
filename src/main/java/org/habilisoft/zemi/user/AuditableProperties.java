package org.habilisoft.zemi.user;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public record AuditableProperties(
        LocalDateTime createdAt,
        Username createdBy,
        LocalDateTime updatedAt,
        Username updatedBy
) implements Serializable {
    public static AuditableProperties of(LocalDateTime createdAt, Username createdBy) {
        return new AuditableProperties(createdAt, createdBy, null, null);
    }

    public AuditableProperties update(LocalDateTime updatedAt, Username updatedBy) {
        return new AuditableProperties(createdAt, createdBy, updatedAt, updatedBy);
    }
}
