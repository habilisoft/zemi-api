package org.habilisoft.zemi.util;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class Commands {
    public static String user = "test";
    @NotNull
    public static LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }
    public class Users {
        public static CreateRoleBuilder adminRole() {
            return createRoleBuilder()
                    .name(RoleName.from("admin"))
                    .description("Administrator Role")
                    .permissions(Set.of(PermissionName.from("auth:role:create")));
        }
        @SuppressWarnings("unused")
        @Builder(builderMethodName = "createRoleBuilder")
        public static org.habilisoft.zemi.user.usecase.Commands.CreateRole createRole(RoleName name,
                                                                                      String description,
                                                                                      Set<PermissionName> permissions,
                                                                                      String user,
                                                                                      LocalDateTime time) {
            return new org.habilisoft.zemi.user.usecase.Commands.CreateRole(name, description, permissions,
                    Optional.ofNullable(user).orElse(org.habilisoft.zemi.util.Commands.user),
                    Optional.ofNullable(time).orElse(now()));
        }
        @SuppressWarnings("unused")
        @Builder(builderMethodName = "createUserBuilder")
        public static org.habilisoft.zemi.user.usecase.Commands.CreateUser createUser(Username username,
                                                                                      String name,
                                                                                      String password,
                                                                                      Boolean changePasswordAtNextLogin,
                                                                                      Set<RoleName> roles,
                                                                                      String user,
                                                                                      LocalDateTime time) {
            return new org.habilisoft.zemi.user.usecase.Commands.CreateUser(username, name, password, changePasswordAtNextLogin, roles,
                    Optional.ofNullable(user).orElse(org.habilisoft.zemi.util.Commands.user), Optional.ofNullable(time).orElse(now()));
        }
    }
}
