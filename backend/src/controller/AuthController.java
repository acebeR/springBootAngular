package com.exemplo.projeto.controller;

import com.exemplo.projeto.model.Usuario;
import com.exemplo.projeto.repository.UsuarioRepository;
import com.exemplo.projeto.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository repo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // ==================== Cadastro ====================
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody Usuario body) {
        if (repo.findByUsername(body.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username_taken"));
        }

        body.setPassword(encoder.encode(body.getPassword()));
        repo.save(body);

        String token = jwtUtil.generateToken(body.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ==================== Login ====================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario body) {
        var usuarioOpt = repo.findByUsername(body.getUsername());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }

        var usuario = usuarioOpt.get();

        if (!encoder.matches(body.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }

        // Gera token
        String token = jwtUtil.generateToken(usuario.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
