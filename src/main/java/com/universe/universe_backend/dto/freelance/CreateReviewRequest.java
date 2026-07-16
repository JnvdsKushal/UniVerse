// dto/freelance/CreateReviewRequest.java
package com.universe.universe_backend.dto.freelance;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateReviewRequest {
    @Min(1) @Max(5) private int rating;
    private String comment;
}