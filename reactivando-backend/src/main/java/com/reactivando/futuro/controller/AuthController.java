package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.UsuarioResponseDTO;
import com.reactivando.futuro.dto.auth.AuthResponseDTO;
import com.reactivando.futuro.dto.auth.LoginRequestDTO;
import com.reactivando.futuro.dto.auth.RegisterRequestDTO;
import com.reactivando.futuro.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        AuthResponseDTO authResponse = authService.login(loginDTO);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerDTO) {
        AuthResponseDTO authResponse = authService.register(registerDTO);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UsuarioResponseDTO usuarioActual = authService.obtenerUsuarioActual(authentication.getName());
        return ResponseEntity.ok(usuarioActual);
    }
}
