package com.universe.universe_backend.controller;

import com.universe.universe_backend.dto.product.CreateProductRequest;
import com.universe.universe_backend.dto.product.ProductResponse;
import com.universe.universe_backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.create(email, request));
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ProductResponse> addImages(
            @PathVariable UUID id,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(productService.addImages(id, files));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> search(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(productService.search(categoryId, minPrice, maxPrice, keyword, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @AuthenticationPrincipal String email) {
        productService.delete(id, email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/wishlist")
    public ResponseEntity<Void> toggleWishlist(@PathVariable UUID id, @AuthenticationPrincipal String email) {
        productService.toggleWishlist(email, id);
        return ResponseEntity.ok().build();
    }
}