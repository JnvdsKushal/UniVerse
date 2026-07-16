package com.universe.universe_backend.controller;

import com.universe.universe_backend.dto.user.UpdateProfileRequest;
import com.universe.universe_backend.dto.user.UserProfileResponse;
import com.universe.universe_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMe(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(email, request));
    }
    
    @PostMapping("/me/avatar")
    public ResponseEntity<UserProfileResponse> uploadAvatar(
            @AuthenticationPrincipal String email,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.uploadAvatar(email, file));
    }
}