// repository/ReviewRepository.java
package com.universe.universe_backend.repository;
import com.universe.universe_backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {}