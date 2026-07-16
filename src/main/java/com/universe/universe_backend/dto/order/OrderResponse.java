// dto/order/OrderResponse.java
package com.universe.universe_backend.dto.order;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data @AllArgsConstructor
public class OrderResponse {
    private UUID id;
    private UUID productId;
    private String productTitle;
    private String buyerName;
    private String sellerName;
    private String status;
    private String meetupNote;
    private BigDecimal agreedPrice;
    private OffsetDateTime createdAt;
}