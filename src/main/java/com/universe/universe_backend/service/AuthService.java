package com.universe.universe_backend.service;

import com.universe.universe_backend.dto.auth.AuthResponse;
import com.universe.universe_backend.dto.auth.RegisterRequest;
import com.universe.universe_backend.entity.User;
import com.universe.universe_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .universityName(request.getUniversityName())
                .verified(false)
                .role("STUDENT")
                .build();

        User saved = userRepository.save(user);

        return new AuthResponse(saved.getId(), saved.getEmail(), "Registration successful");
    }
}