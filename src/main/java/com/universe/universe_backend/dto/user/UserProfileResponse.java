package com.universe.universe_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String universityName;
    private boolean verified;
    private String role;
    
    private String avatarUrl;
    private int trustScore;
}