package com.universe.universe_backend.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data @AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String condition;
    private String listingType;
    private String status;
    private String categoryName;
    private String sellerName;
    private List<String> imageUrls;
    private OffsetDateTime createdAt;
}