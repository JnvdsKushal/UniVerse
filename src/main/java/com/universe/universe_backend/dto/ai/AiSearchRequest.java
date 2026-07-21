// dto/ai/AiSearchRequest.java
package com.universe.universe_backend.dto.ai;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiSearchRequest {
    @NotBlank
    private String query; // e.g. "cheap used textbooks under $30"
}