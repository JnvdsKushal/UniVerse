// repository/BookingRepository.java
package com.universe.universe_backend.repository;
import com.universe.universe_backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByBuyerId(UUID buyerId);
    List<Booking> findByFreelancerId(UUID freelancerId);
}