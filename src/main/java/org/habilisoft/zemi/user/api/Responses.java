package org.habilisoft.zemi.user.api;

import java.util.List;

public interface Responses {
    record User(String username, String name, Boolean changePasswordAtNextLogin, List<String> permissions) { }
}
