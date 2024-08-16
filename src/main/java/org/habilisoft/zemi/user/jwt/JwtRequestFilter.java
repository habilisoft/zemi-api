package org.habilisoft.zemi.user.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.Role;
import org.habilisoft.zemi.user.domain.User;
import org.habilisoft.zemi.user.domain.UserAndRole;
import org.habilisoft.zemi.user.domain.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtTokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {
        Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(c -> c.getName().equals(AuthConstants.COOKIE_ACCESS_TOKEN_NAME))
                .findAny()
                .map(Cookie::getValue)
                .ifPresent(jwtToken -> authenticateWithToken(jwtToken, request, response));
        chain.doFilter(request, response);
    }

    public void authenticateWithToken(String jwtToken, HttpServletRequest request, HttpServletResponse response) {

        try {
            if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
                return;
            }
            Optional.ofNullable(tokenUtil.getUsernameFromToken(jwtToken))
                    .map(Username::of)
                    .filter(username -> Boolean.TRUE.equals(tokenUtil.validateToken(jwtToken, username)))
                    .map(userRepository::findUserAndRoles)
                    .filter(Predicate.not(List::isEmpty))
                    .ifPresent(userAndRoles -> {
                        User user = userAndRoles.getFirst().getUser();
                        List<Role> roles = userAndRoles.stream().map(UserAndRole::getRole).toList();
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, getAuthorities(roles));
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    });

        } catch (ExpiredJwtException e) {
            log.info("JWT Token has expired");
        } catch (UsernameNotFoundException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            response.addCookie(tokenUtil.getLogoutCookie());
        }
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<Role> roles) {

        List<SimpleGrantedAuthority> roleAuthorities = roles.stream()
                .filter(Role::isSystemRole)
                .map(Role::getName)
                .map(r -> new SimpleGrantedAuthority(r.value()))
                .collect(Collectors.toList());

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(p -> new SimpleGrantedAuthority(p.value()))
                .toList();
        roleAuthorities.addAll(authorities);
        return roleAuthorities;
    }
}
