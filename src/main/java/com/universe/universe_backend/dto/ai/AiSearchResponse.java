// dto/ai/AiSearchResponse.java
package com.universe.universe_backend.dto.ai;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import com.universe.universe_backend.dto.product.ProductResponse;

@Data @AllArgsConstructor
public class AiSearchResponse {
    private String interpretedAs; // human-readable summary of what the AI understood
    private Page<ProductResponse> results;
}