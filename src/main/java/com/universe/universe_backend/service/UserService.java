package com.universe.universe_backend.service;

import com.cloudinary.Cloudinary;
import com.universe.universe_backend.dto.user.UpdateProfileRequest;
import com.universe.universe_backend.dto.user.UserProfileResponse;
import com.universe.universe_backend.entity.User;
import com.universe.universe_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    // ==========================
    // GET PROFILE
    // ==========================
    public UserProfileResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return toResponse(user);
    }

    // ==========================
    // UPDATE PROFILE
    // ==========================
    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(request.getFullName());
        user.setUniversityName(request.getUniversityName());

        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    // ==========================
    // UPLOAD AVATAR
    // ==========================
    public UserProfileResponse uploadAvatar(String email, MultipartFile file) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of("folder", "universe/avatars")
        );

        String url = (String) uploadResult.get("secure_url");

        user.setAvatarUrl(url);

        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    // ==========================
    // ENTITY → DTO
    // ==========================
    private UserProfileResponse toResponse(User user) {

        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUniversityName(),
                user.isVerified(),
                user.getRole(),
                user.getAvatarUrl(),
                user.getTrustScore()
        );
    }
}