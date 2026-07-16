package com.universe.universe_backend.controller;

import com.universe.universe_backend.dto.freelance.*;
import com.universe.universe_backend.service.FreelanceService_;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/freelance")
@RequiredArgsConstructor
public class FreelanceController {

    private final FreelanceService_ freelanceService;

    @PostMapping("/services")
    public ResponseEntity<ServiceResponse> createService(
            @AuthenticationPrincipal String email, @Valid @RequestBody CreateServiceRequest req) {
        return ResponseEntity.ok(freelanceService.createService(email, req));
    }

    @GetMapping("/services")
    public ResponseEntity<Page<ServiceResponse>> listServices(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(freelanceService.listServices(PageRequest.of(page, size)));
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceResponse> getService(@PathVariable UUID id) {
        return ResponseEntity.ok(freelanceService.getService(id));
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> createBooking(
            @AuthenticationPrincipal String email, @RequestParam UUID packageId) {
        return ResponseEntity.ok(freelanceService.createBooking(email, packageId));
    }

    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<BookingResponse> updateStatus(
            @PathVariable UUID id, @AuthenticationPrincipal String email, @RequestParam String status) {
        return ResponseEntity.ok(freelanceService.updateStatus(id, email, status));
    }

    @GetMapping("/bookings/mine")
    public ResponseEntity<List<BookingResponse>> myBookings(
            @AuthenticationPrincipal String email, @RequestParam(defaultValue = "false") boolean asFreelancer) {
        return ResponseEntity.ok(freelanceService.myBookings(email, asFreelancer));
    }

    @PostMapping("/bookings/{id}/review")
    public ResponseEntity<Void> addReview(
            @PathVariable UUID id, @AuthenticationPrincipal String email, @Valid @RequestBody CreateReviewRequest req) {
        freelanceService.addReview(id, email, req);
        return ResponseEntity.ok().build();
    }
}