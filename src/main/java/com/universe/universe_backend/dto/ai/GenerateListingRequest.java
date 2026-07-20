// dto/ai/GenerateListingRequest.java
package com.universe.universe_backend.dto.ai;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateListingRequest {
    @NotBlank
    private String roughDescription; // e.g. "used calc textbook, some highlighting, 8th edition"
    private String category; // optional context, e.g. "Textbooks"
}