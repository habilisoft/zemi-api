package org.habilisoft.zemi.user;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.usecase.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CreateUserUseCase createUserUseCase;
    private final CreateRoleUseCase createRoleUseCase;
    private final EditRoleUseCase editRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final AddRolesToUserUseCase addRolesToUserUseCase;
    private final AssignRoleToUsersUseCase assignRoleToUsersUseCase;
    private final RemoveRoleFromUserUseCase removeRolesFromUserUseCase;
    private final AddPermissionsToRoleUseCase addPermissionsToRoleUseCase;
    private final RemovePermissionsFromRoleUseCase removePermissionsFromRoleUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public String getCurrentUser() {
        return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().value();
    }

    public void createUser(UserCommands.CreateUser createUser) {
        createUserUseCase.execute(createUser);
    }

    public void createRole(UserCommands.CreateRole createRole) {
        createRoleUseCase.execute(createRole);
    }
    public void editRole(UserCommands.EditRole editRole){
        editRoleUseCase.execute(editRole);
    }
    public void deleteRole(UserCommands.DeleteRole role){
        deleteRoleUseCase.execute(role);
    }
    public void addRolesToUser(UserCommands.AddRolesToUser addRolesToUser){
        addRolesToUserUseCase.execute(addRolesToUser);
    }
    public void assignRoleToUsers(UserCommands.AssignRoleToUsers assignRoleToUsers){
        assignRoleToUsersUseCase.execute(assignRoleToUsers);
    }
    public void removeRoleFromUser(UserCommands.RemoveRolesFromUser removeRolesFromUser){
        removeRolesFromUserUseCase.execute(removeRolesFromUser);
    }
    public void addPermissionsToRole(UserCommands.AddPermissionsToRole addPermissionsToRole){
        addPermissionsToRoleUseCase.execute(addPermissionsToRole);
    }
    public void removePermissionsFromRole(UserCommands.RemovePermissionsFromRole removePermissionsFromRole){
        removePermissionsFromRoleUseCase.execute(removePermissionsFromRole);
    }
    public void changePassword(UserCommands.ChangePassword changePassword){
        changePasswordUseCase.execute(changePassword);
    }
    public void resetPassword(UserCommands.ResetPassword resetPassword){
        resetPasswordUseCase.execute(resetPassword);
    }
    public void deleteUser(UserCommands.DeleteUser deleteUser){
        deleteUserUseCase.execute(deleteUser);
    }
}
