package org.habilisoft.zemi.user;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.user.command.CreateRole;
import org.habilisoft.zemi.user.command.CreateUser;
import org.habilisoft.zemi.user.usecase.CreateRoleUseCase;
import org.habilisoft.zemi.user.usecase.CreateUserUseCase;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CreateUserUseCase createUserUseCase;
    private final CreateRoleUseCase createRoleUseCase;

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void createUser(CreateUser createUser) {
        createUserUseCase.execute(createUser);
    }

    public void createRole(CreateRole createRole) {
        createRoleUseCase.execute(createRole);
    }
}
