package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.OrderDto;
import com.example.store_management_tool.controller.dto.OrderResponseDto;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
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

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void deleteOrderById(UUID id) {

        Order order = getOrder(id);

        checkAuthorisation(order.getUser());

        orderRepository.delete(order);
    }

    @Transactional
    public void placeOrder(OrderDto orderDto) {

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(retrieveCurrentUser());
        order.setPaymentMethod(orderDto.getPaymentMethod());

        orderRepository.saveAndFlush(order);

        double totalPrice = orderDto.getItemDtoList().stream().map(
                orderItemDto -> {
                    Product product = productRepository.findById(orderItemDto.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(orderItemDto.getProductId().toString()));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(UUID.randomUUID());
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemDto.getQuantity());
                    orderItem.setPrice(product.getPrice() * orderItemDto.getQuantity());

                    orderItemRepository.save(orderItem);
                    return orderItem.getPrice();
                }
        ).reduce(0.0, Double::sum);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    public OrderResponseDto getOrderDetails(UUID id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id.toString()));

        checkAuthorisation(order.getUser());

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setPaymentMethod(order.getPaymentMethod());
        orderResponseDto.setTotalPrice(order.getTotalPrice());

        return orderResponseDto;
    }

    public List<OrderResponseDto> getOrdersByUser(UUID userId) {

        if (!retrieveCurrentUser().getId().equals(userId) && retrieveUserDetails().getAuthorities().stream().noneMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException(retrieveCurrentUser().getId().toString());
        }
        List<Order> ordersByUserId = orderRepository.findOrdersByUserId(userId);
        return ordersByUserId.stream().map(order ->
                        new OrderResponseDto(order.getId(), order.getTotalPrice(), order.getPaymentMethod()))
                .collect(Collectors.toList());


    }

    private void checkAuthorisation(User user) {
        if (!user.equals(userService.getUserByEmail(retrieveUserDetails().getUsername())) &&
                retrieveUserDetails().getAuthorities().stream().noneMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException(retrieveCurrentUser().getId().toString());
        }
    }

    private Order getOrder(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id.toString()));
    }


    private UserDetails retrieveUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private User retrieveCurrentUser() {
        return userService.getUserByEmail(retrieveUserDetails().getUsername());
    }
}
