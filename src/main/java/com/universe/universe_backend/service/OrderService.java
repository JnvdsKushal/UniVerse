package com.universe.universe_backend.service;

import com.universe.universe_backend.dto.order.CreateOrderRequest;
import com.universe.universe_backend.dto.order.OrderResponse;
import com.universe.universe_backend.entity.Order;
import com.universe.universe_backend.entity.Product;
import com.universe.universe_backend.entity.User;
import com.universe.universe_backend.repository.OrderRepository;
import com.universe.universe_backend.repository.ProductRepository;
import com.universe.universe_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private static final List<String> VALID_TRANSITIONS = List.of(
        "REQUESTED->CONFIRMED", "REQUESTED->CANCELLED",
        "CONFIRMED->MET", "CONFIRMED->CANCELLED",
        "MET->COMPLETED"
    );

    public OrderResponse createOrder(String buyerEmail, CreateOrderRequest req) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getSeller().getId().equals(buyer.getId())) {
            throw new IllegalStateException("Cannot buy your own listing");
        }
        if (!"ACTIVE".equals(product.getStatus())) {
            throw new IllegalStateException("This listing is no longer available");
        }

        Order order = Order.builder()
                .product(product)
                .buyer(buyer)
                .seller(product.getSeller())
                .agreedPrice(req.getAgreedPrice())
                .meetupNote(req.getMeetupNote())
                .build();

        return toResponse(orderRepository.save(order));
    }

    public OrderResponse updateStatus(UUID orderId, String requesterEmail, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        boolean isParty = order.getBuyer().getEmail().equals(requesterEmail)
                || order.getSeller().getEmail().equals(requesterEmail);
        if (!isParty) throw new SecurityException("Not authorized for this order");

        String transitionKey = order.getStatus() + "->" + newStatus;
        if (!VALID_TRANSITIONS.contains(transitionKey)) {
            throw new IllegalStateException("Invalid status transition: " + transitionKey);
        }

        order.setStatus(newStatus);

        if ("COMPLETED".equals(newStatus)) {
            Product product = order.getProduct();
            product.setStatus("SOLD");
            productRepository.save(product);
        }

        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> myOrders(String email, boolean asSeller) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Order> orders = asSeller
                ? orderRepository.findBySellerId(user.getId())
                : orderRepository.findByBuyerId(user.getId());
        return orders.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order o) {
        return new OrderResponse(
                o.getId(), o.getProduct().getId(), o.getProduct().getTitle(),
                o.getBuyer().getFullName(), o.getSeller().getFullName(),
                o.getStatus(), o.getMeetupNote(), o.getAgreedPrice(), o.getCreatedAt()
        );
    }
}