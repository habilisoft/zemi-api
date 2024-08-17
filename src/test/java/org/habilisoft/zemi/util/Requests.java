package org.habilisoft.zemi.util;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.habilisoft.zemi.user.jwt.JwtRequest;

@UtilityClass
public class Requests {
    public class Users {
        @SuppressWarnings("unused")
        @Builder(builderMethodName = "loginBuilder")
        public static JwtRequest login(String username, String password) {
            return new JwtRequest(username, password);
        }
    }
}
