package com.sgurin.inetback.service;

import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.security.TokenProvider;
import com.sgurin.inetback.utils.IPHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class TokenAuthService {
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;

    @Autowired
    public TokenAuthService(UserService userService, AuthenticationManager authManager,
                            TokenProvider tokenProvider) {
        this.userService = userService;
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
    }

    public UserDetails getPrincipal() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User getCurrentUser() {
        User user = null;
        try {
            user = userService.findByEmail(getPrincipal().getUsername());

            if (Objects.isNull(user)){
                throw new BadCredentialsException("user.credentials.wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("user.credentials.wrong");
        }
        return user;
    }

    public String addAuthentication(User user, HttpServletRequest request) throws AuthenticationException {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String extractIp = IPHandler.extractIp(request);

        return tokenProvider.createToken(authentication, extractIp);
    }
}
