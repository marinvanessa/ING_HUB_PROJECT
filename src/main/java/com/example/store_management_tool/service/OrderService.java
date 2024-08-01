package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.OrderDto;
import com.example.store_management_tool.controller.dto.OrderResponseDto;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.repository.UserRepository;
import com.example.store_management_tool.service.exception.AccessForbiddenException;
import com.example.store_management_tool.service.exception.OrderNotFoundException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import com.example.store_management_tool.service.model.Order;
import com.example.store_management_tool.service.model.OrderItem;
import com.example.store_management_tool.service.model.Product;
import com.example.store_management_tool.service.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void deleteOrderById(UUID id) {
        orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id.toString()));
    }

    @Transactional
    public void placeOrder(OrderDto orderDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(user);
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setPaymentMethod(orderDto.getPaymentMethod());

        orderRepository.save(order);

        orderDto.getItemDtoList().stream().map(
                orderItemDto -> {
                    Product product = productRepository.findById(orderItemDto.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(orderItemDto.getProductId().toString()));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(UUID.randomUUID());
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemDto.getQuantity());
                    orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
                    return orderItem;
                }
        ).forEach(orderItemRepository::save);
    }

    public OrderResponseDto getOrderDetails(UUID id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id.toString()));

        if (!order.getUser().equals(userService.getUserByEmail(userDetails.getUsername())) &&
                userDetails.getAuthorities().stream().noneMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to see order details");

        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setPaymentMethod(order.getPaymentMethod());
        orderResponseDto.setTotalPrice(order.getTotalPrice());

        return orderResponseDto;
    }

    public List<OrderResponseDto> getOrdersByUser(UUID userId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        if (!currentUser.getId().equals(userId) && userDetails.getAuthorities().stream().noneMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
        }
        List<Order> ordersByUserId = orderRepository.findOrdersByUserId(userId);
        return ordersByUserId.stream().map(order ->
                new OrderResponseDto(order.getId(), order.getTotalPrice(), order.getPaymentMethod()))
                .collect(Collectors.toList());


    }
}
