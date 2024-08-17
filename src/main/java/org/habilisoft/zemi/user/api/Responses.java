package org.habilisoft.zemi.user.api;

import org.habilisoft.zemi.user.Username;

import java.util.List;

public interface Responses {
    record User(Username username, String name, Boolean changePasswordAtNextLogin, List<String> permissions) { }
}
