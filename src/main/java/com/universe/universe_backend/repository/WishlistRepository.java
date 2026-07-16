package com.universe.universe_backend.repository;

import com.universe.universe_backend.entity.Wishlist;
import com.universe.universe_backend.entity.WishlistId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {
    List<Wishlist> findByUserId(UUID userId);
}