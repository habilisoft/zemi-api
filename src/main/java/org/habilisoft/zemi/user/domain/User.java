package org.habilisoft.zemi.user.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.exception.RoleAlreadyOnUserException;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "username")
@SQLRestriction(value = "deleted is not true")
@SQLDelete(sql = "update users set deleted = true where username = ?")
public class User implements Serializable {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private Username username;
    private String name;
    private String password;
    private Boolean deleted;
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;
    private Boolean changePasswordAtNextLogin;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "username"))
    @AttributeOverride(name = "value", column = @Column(name = "role_name"))
    private Set<RoleName> roles;

    public static User create(Username username, String name, String password, Boolean changePasswordAtNextLogin, Set<RoleName> roles, LocalDateTime createdAt, Username createdBy) {
        User user = new User();
        user.username = username;
        user.name = name;
        user.password = password;
        user.deleted = false;
        user.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        user.changePasswordAtNextLogin = changePasswordAtNextLogin;
        user.roles = roles;
        return user;
    }

    public void setPassword(String password, Boolean changePasswordAtNextLogin, LocalDateTime updatedAt, Username updatedBy) {
        this.password = password;
        this.changePasswordAtNextLogin = changePasswordAtNextLogin;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
    public void removeRoles(Set<RoleName> roles, LocalDateTime updatedAt, Username updatedBy) {
        roles.forEach(this.roles::remove);
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
    public void addRoles(Set<RoleName> roles, LocalDateTime updatedAt, Username updatedBy) {
        this.roles.stream().filter(roles::contains).findAny().ifPresent(r -> {
            throw new RoleAlreadyOnUserException(this.username, r);
        });
        this.roles.addAll(roles);
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
    public void delete(LocalDateTime updatedAt, Username updatedBy) {
        this.deleted = true;
        this.auditableProperties = this.auditableProperties.update(updatedAt, updatedBy);
    }
}
