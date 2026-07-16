// dto/order/CreateOrderRequest.java
package com.universe.universe_backend.dto.order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateOrderRequest {
    @NotNull private UUID productId;
    @NotNull private BigDecimal agreedPrice;
    private String meetupNote;
}