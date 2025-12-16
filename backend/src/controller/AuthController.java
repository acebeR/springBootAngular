package com.exemplo.projeto.controller;

import com.exemplo.projeto.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AuthController(UserDetailsService userDetailsService, JwtUtil jwtUtil, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(username);
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }
    }
}
