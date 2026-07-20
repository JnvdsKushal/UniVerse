// dto/ai/GenerateListingResponse.java
package com.universe.universe_backend.dto.ai;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data @AllArgsConstructor
public class GenerateListingResponse {
    private String suggestedTitle;
    private String polishedDescription;
    private BigDecimal suggestedPrice;
    private String priceReasoning;
}