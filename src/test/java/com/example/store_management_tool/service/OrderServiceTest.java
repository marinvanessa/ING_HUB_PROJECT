package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.OrderResponseDto;
import com.example.store_management_tool.coverter.OrderItemConverter;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.enums.PaymentMethod;
import com.example.store_management_tool.service.enums.Role;
import com.example.store_management_tool.service.model.Order;
import com.example.store_management_tool.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemConverter orderItemConverter;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository, userService, productRepository,
                orderItemRepository, orderItemConverter);
    }

    @Test
    public void getOrders() {
        User user = new User();
        user.setRole(Role.ROLE_USER);
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setTotalPrice(10.0);
        order.setUser(user);

        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order));
        Mockito.when(orderItemRepository.findAllByOrderId(Mockito.any(UUID.class))).thenReturn(List.of());

        List<OrderResponseDto> result = orderService.getOrders();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(PaymentMethod.CREDIT_CARD, result.get(0).getPaymentMethod());
        Assertions.assertEquals(10.0, result.get(0).getTotalPrice());
    }

    @Test
    public void deleteOrderById() {
        setUpAuthorization();
        UUID orderId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        orderService.deleteOrderById(orderId);
        Mockito.verify(orderItemRepository).deleteAll(Mockito.anyList());
        Mockito.verify(orderRepository).delete(order);
    }

    @Test
    public void getOrderDetails() {
        setUpAuthorization();
        UUID orderId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        Order order = new Order();
        order.setId(orderId);
        order.setTotalPrice(100.0);
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setUser(user);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(List.of());
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        OrderResponseDto result = orderService.getOrderDetails(orderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderId, result.getId());
        Mockito.verify(orderRepository).findById(orderId);
    }

    @Test
    public void getOrdersByUser() {
        setUpAuthorization();
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setRole(Role.ROLE_ADMIN);
        Order order = new Order();
        order.setId(orderId);
        order.setTotalPrice(100.0);
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setUser(user);

        Mockito.when(orderRepository.findOrdersByUserId(userId)).thenReturn(List.of(order));
        Mockito.when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(List.of());
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        List<OrderResponseDto> result = orderService.getOrdersByUser(userId);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(orderId, result.get(0).getId());
        Mockito.verify(orderRepository).findOrdersByUserId(userId);
    }

    private void setUpAuthorization() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("test@example.com");
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
