package com.universe.universe_backend.controller;

import com.universe.universe_backend.dto.order.CreateOrderRequest;
import com.universe.universe_backend.dto.order.OrderResponse;
import com.universe.universe_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @AuthenticationPrincipal String email, @Valid @RequestBody CreateOrderRequest req) {
        return ResponseEntity.ok(orderService.createOrder(email, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable UUID id, @AuthenticationPrincipal String email, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, email, status));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<OrderResponse>> myOrders(
            @AuthenticationPrincipal String email, @RequestParam(defaultValue = "false") boolean asSeller) {
        return ResponseEntity.ok(orderService.myOrders(email, asSeller));
    }
}