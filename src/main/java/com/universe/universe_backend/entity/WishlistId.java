package com.universe.universe_backend.entity;

import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class WishlistId implements Serializable {
    private UUID userId;
    private UUID productId;
}