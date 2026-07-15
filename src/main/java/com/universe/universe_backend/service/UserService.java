package com.universe.universe_backend.service;

import com.universe.universe_backend.dto.user.UpdateProfileRequest;
import com.universe.universe_backend.dto.user.UserProfileResponse;
import com.universe.universe_backend.entity.User;
import com.universe.universe_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toResponse(user);
    }

    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(request.getFullName());
        user.setUniversityName(request.getUniversityName());
        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    private UserProfileResponse toResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUniversityName(),
                user.isVerified(),
                user.getRole()
        );
    }
}