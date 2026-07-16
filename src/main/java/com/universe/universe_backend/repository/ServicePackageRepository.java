// repository/ServicePackageRepository.java
package com.universe.universe_backend.repository;
import com.universe.universe_backend.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ServicePackageRepository extends JpaRepository<ServicePackage, UUID> {}