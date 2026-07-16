// dto/freelance/CreateServiceRequest.java
package com.universe.universe_backend.dto.freelance;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class CreateServiceRequest {
    @NotBlank private String title;
    private String description;
    private String category;
    private List<PackageInput> packages;

    @Data
    public static class PackageInput {
        @NotBlank private String tier;
        private java.math.BigDecimal price;
        private int deliveryDays;
        private String description;
    }
}