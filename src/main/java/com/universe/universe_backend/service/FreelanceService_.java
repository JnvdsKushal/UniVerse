package com.universe.universe_backend.service;

import com.universe.universe_backend.dto.freelance.*;
import com.universe.universe_backend.entity.*;
import com.universe.universe_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreelanceService_ {

    private final FreelanceServiceRepository serviceRepository;
    private final ServicePackageRepository packageRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private static final List<String> VALID_TRANSITIONS_KEYS = List.of(
        "PENDING->ACCEPTED", "PENDING->REJECTED",
        "ACCEPTED->IN_PROGRESS", "IN_PROGRESS->DELIVERED",
        "DELIVERED->COMPLETED", "PENDING->CANCELLED", "ACCEPTED->CANCELLED"
    );

    public ServiceResponse createService(String freelancerEmail, CreateServiceRequest req) {
        User freelancer = userRepository.findByEmail(freelancerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        com.universe.universe_backend.entity.FreelanceService service =
                com.universe.universe_backend.entity.FreelanceService.builder()
                        .freelancer(freelancer)
                        .title(req.getTitle())
                        .description(req.getDescription())
                        .category(req.getCategory())
                        .build();

        com.universe.universe_backend.entity.FreelanceService saved = serviceRepository.save(service);

        if (req.getPackages() != null) {
            for (var p : req.getPackages()) {
                ServicePackage pkg = ServicePackage.builder()
                        .service(saved)
                        .tier(p.getTier())
                        .price(p.getPrice())
                        .deliveryDays(p.getDeliveryDays())
                        .description(p.getDescription())
                        .build();
                saved.getPackages().add(packageRepository.save(pkg));
            }
        }
        return toResponse(saved);
    }

    public ServiceResponse getService(UUID id) {
        return toResponse(serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found")));
    }

    public org.springframework.data.domain.Page<ServiceResponse> listServices(org.springframework.data.domain.Pageable pageable) {
        return serviceRepository.findByStatus("ACTIVE", pageable).map(this::toResponse);
    }

    public BookingResponse createBooking(String buyerEmail, UUID packageId) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ServicePackage pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        Booking booking = Booking.builder()
                .servicePackage(pkg)
                .buyer(buyer)
                .freelancer(pkg.getService().getFreelancer())
                .build();

        return toBookingResponse(bookingRepository.save(booking));
    }

    public BookingResponse updateStatus(UUID bookingId, String requesterEmail, String newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        boolean isParty = booking.getBuyer().getEmail().equals(requesterEmail)
                || booking.getFreelancer().getEmail().equals(requesterEmail);
        if (!isParty) throw new SecurityException("Not authorized for this booking");

        String transitionKey = booking.getStatus() + "->" + newStatus;
        if (!VALID_TRANSITIONS_KEYS.contains(transitionKey)) {
            throw new IllegalStateException("Invalid status transition: " + transitionKey);
        }

        booking.setStatus(newStatus);
        return toBookingResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> myBookings(String email, boolean asFreelancer) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Booking> bookings = asFreelancer
                ? bookingRepository.findByFreelancerId(user.getId())
                : bookingRepository.findByBuyerId(user.getId());
        return bookings.stream().map(this::toBookingResponse).collect(Collectors.toList());
    }

    public void addReview(UUID bookingId, String reviewerEmail, CreateReviewRequest req) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (!"COMPLETED".equals(booking.getStatus())) {
            throw new IllegalStateException("Can only review completed bookings");
        }
        User reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Review review = Review.builder()
                .booking(booking)
                .reviewer(reviewer)
                .rating(req.getRating())
                .comment(req.getComment())
                .build();
        reviewRepository.save(review);
    }

    private ServiceResponse toResponse(com.universe.universe_backend.entity.FreelanceService s) {
        return new ServiceResponse(
                s.getId(), s.getTitle(), s.getDescription(), s.getCategory(), s.getStatus(),
                s.getFreelancer().getFullName(),
                s.getPackages().stream().map(p -> new ServiceResponse.PackageResponse(
                        p.getId(), p.getTier(), p.getPrice(), p.getDeliveryDays(), p.getDescription()
                )).collect(Collectors.toList())
        );
    }

    private BookingResponse toBookingResponse(Booking b) {
        return new BookingResponse(
                b.getId(), b.getServicePackage().getId(),
                b.getServicePackage().getService().getTitle(),
                b.getBuyer().getFullName(), b.getFreelancer().getFullName(),
                b.getStatus(), b.getCreatedAt()
        );
    }
}