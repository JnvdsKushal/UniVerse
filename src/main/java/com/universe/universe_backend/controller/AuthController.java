package com.universe.universe_backend.controller;

import com.universe.universe_backend.dto.auth.AuthResponse;
import com.universe.universe_backend.dto.auth.LoginRequest;
import com.universe.universe_backend.dto.auth.LoginResponse;
import com.universe.universe_backend.dto.auth.RegisterRequest;
import com.universe.universe_backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}