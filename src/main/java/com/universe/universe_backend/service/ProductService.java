package com.universe.universe_backend.service;

import com.cloudinary.Cloudinary;
import com.universe.universe_backend.dto.product.CreateProductRequest;
import com.universe.universe_backend.dto.product.ProductResponse;
import com.universe.universe_backend.entity.*;
import com.universe.universe_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final Cloudinary cloudinary;

    public ProductResponse create(String sellerEmail, CreateProductRequest req) {
        User seller = userRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Category category = req.getCategoryId() != null
                ? categoryRepository.findById(req.getCategoryId()).orElse(null)
                : null;

        Product product = Product.builder()
                .seller(seller)
                .category(category)
                .title(req.getTitle())
                .description(req.getDescription())
                .price(req.getPrice())
                .condition(req.getCondition())
                .listingType(req.getListingType())
                .build();

        return toResponse(productRepository.save(product));
    }

    public ProductResponse addImages(UUID productId, List<MultipartFile> files) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        int position = product.getImages().size();
        for (MultipartFile file : files) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of("folder", "universe/products"));
            String url = (String) uploadResult.get("secure_url");
            product.getImages().add(ProductImage.builder()
                    .product(product)
                    .url(url)
                    .position(position++)
                    .build());
        }
        return toResponse(productRepository.save(product));
    }

    public Page<ProductResponse> search(UUID categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                                          String keyword, Pageable pageable) {
        return productRepository.search(categoryId, minPrice, maxPrice, keyword, pageable)
                .map(this::toResponse);
    }

    public ProductResponse getById(UUID id) {
        return toResponse(productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));
    }

    public void delete(UUID id, String requesterEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (!product.getSeller().getEmail().equals(requesterEmail)) {
            throw new SecurityException("Not authorized to delete this product");
        }
        product.setStatus("DELETED");
        productRepository.save(product);
    }

    public void toggleWishlist(String userEmail, UUID productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        WishlistId id = new WishlistId(user.getId(), productId);
        if (wishlistRepository.existsById(id)) {
            wishlistRepository.deleteById(id);
        } else {
            Wishlist w = Wishlist.builder().userId(user.getId()).productId(productId).build();
            wishlistRepository.save(w);
        }
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(), p.getTitle(), p.getDescription(), p.getPrice(),
                p.getCondition(), p.getListingType(), p.getStatus(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getSeller().getFullName(),
                p.getImages().stream().map(ProductImage::getUrl).collect(Collectors.toList()),
                p.getCreatedAt()
        );
    }
}