package org.habilisoft.zemi.user.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "permissions")
@EqualsAndHashCode(of = "name")
public class Permission {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private PermissionName name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Module module;
}
