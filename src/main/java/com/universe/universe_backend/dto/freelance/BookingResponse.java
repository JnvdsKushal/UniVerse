// dto/freelance/BookingResponse.java
package com.universe.universe_backend.dto.freelance;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data @AllArgsConstructor
public class BookingResponse {
    private UUID id;
    private UUID packageId;
    private String serviceTitle;
    private String buyerName;
    private String freelancerName;
    private String status;
    private OffsetDateTime createdAt;
}