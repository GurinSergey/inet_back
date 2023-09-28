package com.sgurin.inetback.service;

import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenAuthService {
    private final UserRepository userRepository;

    @Autowired
    public TokenAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails getPrincipal() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User getCurrentUser() {
        User user = null;
        try {
            user =  userRepository.findByEmail(getPrincipal().getUsername()).orElseThrow(() -> new BadCredentialsException("user.credentials.wrong"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("user.credentials.wrong");
        }
        return user;
    }
}
