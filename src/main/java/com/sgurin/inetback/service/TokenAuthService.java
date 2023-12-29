package com.sgurin.inetback.service;

import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TokenAuthService {
    private final UserService userService;

    @Autowired
    public TokenAuthService(UserService userService) {
        this.userService = userService;
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
}
