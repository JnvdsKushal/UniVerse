// dto/freelance/ServiceResponse.java
package com.universe.universe_backend.dto.freelance;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data @AllArgsConstructor
public class ServiceResponse {
    private UUID id;
    private String title;
    private String description;
    private String category;
    private String status;
    private String freelancerName;
    private List<PackageResponse> packages;

    @Data @AllArgsConstructor
    public static class PackageResponse {
        private UUID id;
        private String tier;
        private java.math.BigDecimal price;
        private int deliveryDays;
        private String description;
    }
}