package com.sgurin.inetback.controller;

import com.sgurin.inetback.config.security.jwt.JwtTokenUtil;
import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.exeption.UnauthorizedException;
import com.sgurin.inetback.response.GenericResponse;
import com.sgurin.inetback.service.LocaleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthenticationManager authManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.authManager = authManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<GenericResponse<User>> login(@RequestBody @Valid User user) throws UnauthorizedException {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), user.getPassword())
            );

            User currentUser = (User) authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(currentUser);

            return GenericResponse.successWithToken(currentUser, accessToken);

        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException(ex.getMessage());
        }
    }
}
