package org.habilisoft.zemi.util;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.habilisoft.zemi.catalog.category.application.CreateCategory;
import org.habilisoft.zemi.catalog.category.domain.CategoryId;
import org.habilisoft.zemi.catalog.product.application.RegisterProduct;
import org.habilisoft.zemi.sales.customer.application.RegisterCustomer;
import org.habilisoft.zemi.sales.customer.domain.Address;
import org.habilisoft.zemi.sales.customer.domain.Contact;
import org.habilisoft.zemi.sales.customer.domain.CustomerType;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.PermissionName;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.usecase.UserCommands;
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
        public static UserCommands.CreateRole createRole(RoleName name,
                                                         String description,
                                                         Set<PermissionName> permissions,
                                                         String user,
                                                         LocalDateTime time) {
            return new UserCommands.CreateRole(name, description, permissions,
                    Optional.ofNullable(user).orElse(org.habilisoft.zemi.util.Commands.user),
                    Optional.ofNullable(time).orElse(now()));
        }

        @SuppressWarnings("unused")
        @Builder(builderMethodName = "createUserBuilder")
        public static UserCommands.CreateUser createUser(Username username,
                                                         String name,
                                                         String password,
                                                         Boolean changePasswordAtNextLogin,
                                                         Set<RoleName> roles,
                                                         String user,
                                                         LocalDateTime time) {
            return new UserCommands.CreateUser(username, name, password, changePasswordAtNextLogin, roles,
                    Optional.ofNullable(user).orElse(org.habilisoft.zemi.util.Commands.user), Optional.ofNullable(time).orElse(now()));
        }
    }

    public class Catalog {
        @SuppressWarnings("unused")
        @Builder(builderMethodName = "registerProductBuilder")
        public static RegisterProduct registerProduct(String name, CategoryId categoryId, Boolean isService, String user, LocalDateTime time) {
            return new RegisterProduct(Optional.ofNullable(categoryId), name, Optional.ofNullable(isService).orElse(false),
                    Optional.ofNullable(time).orElse(now()), Optional.ofNullable(user).orElse(Commands.user));
        }

        @SuppressWarnings("unused")
        @Builder(builderMethodName = "createCategoryBuilder")
        public static CreateCategory createCategory(String name, String user, LocalDateTime time) {
            return new CreateCategory(name, Optional.ofNullable(time).orElse(now()), Optional.ofNullable(user).orElse(Commands.user));
        }
    }

    public class Sales {
        @SuppressWarnings("unused")
        @Builder(builderMethodName = "registerCustomerBuilder")
        public static RegisterCustomer registerCustomer(String name,
                                                      CustomerType type,
                                                      Contact contact,
                                                      Address address, String user, LocalDateTime time) {
            return new RegisterCustomer(
                    name, type, contact,
                    Optional.ofNullable(address).orElseGet(() -> Address.of("Fake Street", "Fake City", "00000")),
                    Optional.ofNullable(time).orElse(now()), Optional.ofNullable(user).orElse(Commands.user));
        }
    }
}
