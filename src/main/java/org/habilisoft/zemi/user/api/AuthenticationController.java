package org.habilisoft.zemi.user.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.habilisoft.zemi.user.jwt.JwtRequest;
import org.habilisoft.zemi.user.jwt.JwtService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AuthenticationController {
    private final JwtService authenticationService;

    @PostMapping("/authenticate")
    public void createAuthenticationToken(
            @RequestBody JwtRequest authenticationRequest,
            HttpServletResponse response) {

        Cookie cookie = authenticationService.createTokenCookie(authenticationRequest);
        response.addCookie(cookie);
    }

    @GetMapping("/check-token")
    public void checkToken() {
        // eh?
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response)  {
        response.addCookie(authenticationService.getLogoutCookie());
    }
}
