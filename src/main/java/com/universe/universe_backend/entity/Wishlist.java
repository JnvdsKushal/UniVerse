package com.universe.universe_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity @Table(name = "wishlist")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(WishlistId.class)
public class Wishlist {
    @Id
    @Column(name = "user_id")
    private java.util.UUID userId;

    @Id
    @Column(name = "product_id")
    private java.util.UUID productId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = OffsetDateTime.now(); }
}