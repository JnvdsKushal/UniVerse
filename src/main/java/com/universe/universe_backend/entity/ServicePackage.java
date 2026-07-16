// entity/ServicePackage.java
package com.universe.universe_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name = "service_packages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServicePackage {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private FreelanceService service;

    @Column(nullable = false, length = 20)
    private String tier;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "delivery_days", nullable = false)
    private int deliveryDays;

    @Column(columnDefinition = "TEXT")
    private String description;
}