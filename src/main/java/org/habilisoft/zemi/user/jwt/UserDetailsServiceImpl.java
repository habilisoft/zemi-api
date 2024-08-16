package org.habilisoft.zemi.user.jwt;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserAndRole> userAndRoles = userRepository.findUserAndRoles(Username.of(username));
        if (userAndRoles.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        var u = userAndRoles.getFirst().getUser();
        List<Role> roles = userAndRoles.stream().map(UserAndRole::getRole).toList();
        List<PermissionName> permission = roles.stream().flatMap(r -> r.getPermissions().stream()).toList();
        return User.builder()
                .username(username)
                .password(u.getPassword())
                .roles(getRoles(roles))
                .authorities(getAuthorities(permission))
                .build();
    }

    private String[] getRoles(List<Role> roles) {
        return roles.stream()
                .filter(Role::isSystemRole)
                .map(Role::getName)
                .map(RoleName::value)
                .toArray(String[]::new);
    }

    private String[] getAuthorities(List<PermissionName> roles) {
        return roles.stream()
                .map(PermissionName::value)
                .toArray(String[]::new);
    }
}
