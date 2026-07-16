package com.universe.universe_backend.dto.product;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateProductRequest {
    @NotBlank private String title;
    private String description;
    @NotNull @DecimalMin("0.0") private BigDecimal price;
    @NotBlank private String condition;
    @NotBlank private String listingType;
    private UUID categoryId;
}