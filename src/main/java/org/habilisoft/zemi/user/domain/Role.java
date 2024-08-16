package org.habilisoft.zemi.user.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.exception.PermissionAlreadyOnRoleException;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
@EqualsAndHashCode(of = "name")
@SQLRestriction(value = "deleted is not true")
@SQLDelete(sql = "update roles set deleted = true where id = ?")
public class Role {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private RoleName name;
    private String description;
    private Boolean deleted;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_name"))
    @AttributeOverride(name = "value", column = @Column(name = "permission_name"))
    private Set<PermissionName> permissions;

    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;

    public static Role create(RoleName name, String description, Set<PermissionName> permissions, LocalDateTime createdAt, Username createdBy) {
        Role role = new Role();
        role.name = name;
        role.description = description;
        role.permissions = permissions;
        role.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return role;
    }

    public boolean isSystemRole() {
        return Optional.ofNullable(auditableProperties)
                .map(AuditableProperties::createdBy)
                .map(Username::value)
                .map("system"::equals)
                .orElse(false);
    }

    public void delete(LocalDateTime updatedAt, Username updatedBy) {
        deleted = true;
        auditableProperties = auditableProperties.update(updatedAt, updatedBy);
    }

    public void addPermissions(Set<PermissionName> permissions, LocalDateTime updatedAt, Username updatedBy) {
        this.permissions.stream().filter(permissions::contains).findAny().ifPresent(p -> {
            throw new PermissionAlreadyOnRoleException(this.name, p);
        });
        this.permissions.addAll(permissions);
        auditableProperties = auditableProperties.update(updatedAt, updatedBy);
    }

    public void removePermissions(Set<PermissionName> permissions, LocalDateTime updatedAt, Username updatedBy) {
        this.permissions.removeAll(permissions);
        auditableProperties = auditableProperties.update(updatedAt, updatedBy);
    }
}
