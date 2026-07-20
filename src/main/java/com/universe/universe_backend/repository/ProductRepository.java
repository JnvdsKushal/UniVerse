package com.universe.universe_backend.repository;

import com.universe.universe_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	@Query("""
			SELECT p FROM Product p
			WHERE p.status = 'ACTIVE'
			AND (:categoryId IS NULL OR p.category.id = :categoryId)
			AND (:minPrice IS NULL OR p.price >= :minPrice)
			AND (:maxPrice IS NULL OR p.price <= :maxPrice)
			AND (
			    COALESCE(:keyword, '') = ''
			    OR LOWER(p.title) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
			)
			""")
			Page<Product> search(
			        @Param("categoryId") UUID categoryId,
			        @Param("minPrice") BigDecimal minPrice,
			        @Param("maxPrice") BigDecimal maxPrice,
			        @Param("keyword") String keyword,
			        Pageable pageable
			);
}