package org.habilisoft.zemi.user.jwt;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtResponse createAuthenticationToken(JwtRequest authenticationRequest) {
        authenticate(authenticationRequest);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    public Cookie createTokenCookie(JwtRequest authenticationRequest) {
        JwtResponse tokenResponse = createAuthenticationToken(authenticationRequest);
        return getTokenCookie(tokenResponse);
    }

    public Cookie getTokenCookie(JwtResponse tokenResponse) {
        Cookie cookie = new Cookie(AuthConstants.COOKIE_ACCESS_TOKEN_NAME, tokenResponse.token());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void authenticate(JwtRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password())
        );
    }
    public Cookie getLogoutCookie() {
        return jwtTokenUtil.getLogoutCookie();
    }
}
