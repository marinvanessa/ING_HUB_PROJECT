package com.example.store_management_tool.service;

import com.example.store_management_tool.ProductUtil;
import com.example.store_management_tool.controller.dto.OrderDto;
import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.controller.dto.OrderResponseDto;
import com.example.store_management_tool.coverter.OrderItemConverter;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.enums.PaymentMethod;
import com.example.store_management_tool.service.model.Order;
import com.example.store_management_tool.service.model.OrderItem;
import com.example.store_management_tool.service.model.Product;
import com.example.store_management_tool.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
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

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("test@example.com");
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetOrders() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setTotalPrice(10.0);

        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order));
        Mockito.when(orderItemRepository.findAllByOrderId(Mockito.any(UUID.class))).thenReturn(List.of());

        List<OrderResponseDto> result = orderService.getOrders();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(PaymentMethod.CREDIT_CARD, result.get(0).getPaymentMethod());
        Assertions.assertEquals(10.0, result.get(0).getTotalPrice());
    }


    @Test
    public void testPlaceOrder() {
        Product product = ProductUtil.createProduct();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");

        OrderDto orderDto = new OrderDto();
        orderDto.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        List<OrderItemDto> itemDtoList = new ArrayList<>();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderId(UUID.randomUUID());
        orderItemDto.setProductId(product.getId());
        orderItemDto.setQuantity(2);
        orderDto.setItemDtoList(itemDtoList);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID());
        orderItem.setProduct(product);
        orderItem.setPrice(12.3);
        orderItem.setQuantity(3);


        Mockito.when(productRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(product));
        Mockito.when(orderRepository.saveAndFlush(Mockito.any(Order.class))).thenReturn(new Order());
        Mockito.when(orderItemRepository.save(Mockito.any(OrderItem.class))).thenReturn(orderItem);
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        orderService.placeOrder(orderDto);

        Mockito.verify(orderRepository).saveAndFlush(Mockito.any(Order.class));
        Mockito.verify(orderItemRepository).save(Mockito.any(OrderItem.class));
    }

    @Test
    public void testDeleteOrderById() {
        UUID orderId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        Order order = new Order();
        order.setUser(user);
        order.setId(orderId);


        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);
        orderService.deleteOrderById(orderId);
        Mockito.verify(orderItemRepository).deleteAll(Mockito.anyList());
        Mockito.verify(orderRepository).delete(order);
    }

}
