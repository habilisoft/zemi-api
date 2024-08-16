package org.habilisoft.zemi.user;

import lombok.RequiredArgsConstructor;
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
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void createUser(Commands.CreateUser createUser) {
        createUserUseCase.execute(createUser);
    }

    public void createRole(Commands.CreateRole createRole) {
        createRoleUseCase.execute(createRole);
    }
    public void editRole(Commands.EditRole editRole){
        editRoleUseCase.execute(editRole);
    }
    public void deleteRole(Commands.DeleteRole role){
        deleteRoleUseCase.execute(role);
    }
    public void addRolesToUser(Commands.AddRolesToUser addRolesToUser){
        addRolesToUserUseCase.execute(addRolesToUser);
    }
    public void assignRoleToUsers(Commands.AssignRoleToUsers assignRoleToUsers){
        assignRoleToUsersUseCase.execute(assignRoleToUsers);
    }
    public void removeRoleFromUser(Commands.RemoveRolesFromUser removeRolesFromUser){
        removeRolesFromUserUseCase.execute(removeRolesFromUser);
    }
    public void addPermissionsToRole(Commands.AddPermissionsToRole addPermissionsToRole){
        addPermissionsToRoleUseCase.execute(addPermissionsToRole);
    }
    public void removePermissionsFromRole(Commands.RemovePermissionsFromRole removePermissionsFromRole){
        removePermissionsFromRoleUseCase.execute(removePermissionsFromRole);
    }
    public void changePassword(Commands.ChangePassword changePassword){
        changePasswordUseCase.execute(changePassword);
    }
    public void resetPassword(Commands.ResetPassword resetPassword){
        resetPasswordUseCase.execute(resetPassword);
    }
    public void deleteUser(Commands.DeleteUser deleteUser){
        deleteUserUseCase.execute(deleteUser);
    }
}
