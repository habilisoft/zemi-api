package org.habilisoft.zemi.user.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/v1")
public class JwtAuthenticationController {
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
