package org.habilisoft.zemi.user.domain;

import org.habilisoft.zemi.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Username> {
    @Query("select count(u) from User u where :roleName member of u.roles")
    int countUsersByRole(RoleName roleName);

    @Query("""
        select u as user, r as role
        from User u, Role r
        where r.name member of u.roles
        and u.username = :username
    """)
    List<UserAndRole> findUserAndRoles(Username username);
}
