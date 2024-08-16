package org.habilisoft.zemi.util;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.habilisoft.zemi.catalog.api.CreateCategoryRequest;
import org.habilisoft.zemi.catalog.api.RegisterProductRequest;
import org.habilisoft.zemi.user.jwt.JwtRequest;

import java.util.Optional;

@UtilityClass
public class Requests {
    public class Users {
        @SuppressWarnings("unused")
        @Builder(builderMethodName = "loginBuilder")
        public static JwtRequest login(String username, String password) {
            return new JwtRequest(username, password);
        }
    }
    public class Catalog {
        @SuppressWarnings("unused")
        @Builder(builderMethodName = "registerProductBuilder")
        public static RegisterProductRequest registerProduct(Number categoryId, String name) {
            return new RegisterProductRequest(
                    Optional.ofNullable(categoryId).map(Number::longValue).orElse(null),
                    name
            );
        }

        @SuppressWarnings("unused")
        @Builder(builderMethodName = "createCategoryBuilder")
        public static CreateCategoryRequest createCategory(String name) {
            return new CreateCategoryRequest(name);
        }
    }
}
