// repository/FreelanceServiceRepository.java
package com.universe.universe_backend.repository;
import com.universe.universe_backend.entity.FreelanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FreelanceServiceRepository extends JpaRepository<FreelanceService, UUID> {
    Page<FreelanceService> findByStatus(String status, Pageable pageable);
}