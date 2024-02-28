package com.sgurin.inetback.controller;

import com.sgurin.inetback.domain.Role;
import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.enums.AuthProvider;
import com.sgurin.inetback.exeption.BadRequestException;
import com.sgurin.inetback.exeption.UnauthorizedException;
import com.sgurin.inetback.model.SignUpRequest;
import com.sgurin.inetback.repository.RoleRepository;
import com.sgurin.inetback.repository.UserRepository;
import com.sgurin.inetback.response.GenericResponse;
import com.sgurin.inetback.security.TokenProvider;
import com.sgurin.inetback.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TokenAuthService tokenAuthService;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository, TokenAuthService tokenAuthService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tokenAuthService = tokenAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<User>> authenticateUser(@RequestBody User user, HttpServletRequest request) {
        String accessToken;
        try {
            accessToken = tokenAuthService.addAuthentication(user, request);
            user = tokenAuthService.getCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnauthorizedException(e.getMessage());
        }

        return GenericResponse.successWithToken(user, accessToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<GenericResponse<String>> registerUser(@Valid @RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Email address already in use.");
        }

        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setProvider(AuthProvider.local);

        Role role = roleRepository.findByName("ROLE_USER").orElse(null);
        newUser.addRole(role);

        userRepository.save(newUser);

        return GenericResponse.success("Ok");
    }

    @GetMapping("/oauth2/callback/{provider}")
    public ResponseEntity<GenericResponse<String>> callback(@PathVariable(value = "provider") String provider,
                                                            @RequestParam(value = "token", required = false) String token,
                                                            @RequestParam(value = "code", required = false) String code) {
        System.out.println(provider);
        System.out.println(token);
        System.out.println(code);

        return GenericResponse.success(provider);
    }

    @GetMapping("/oauth2/code")
    public ResponseEntity<GenericResponse<String>> callback(@RequestParam(value = "code") String code) {
        System.out.println(code);

        return GenericResponse.success(code);
    }

    @GetMapping("/oauth2/redirect")
    public ResponseEntity<GenericResponse<String>> handleCallback(@RequestParam(value = "token", required = false) String token,
                                                                  @RequestParam(value = "error", required = false) String error) {
        if (error != null){
            return GenericResponse.warning(error);
        }

        return GenericResponse.success(token);
    }
}
