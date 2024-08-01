package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.controller.dto.OrderItemResponseDto;
import com.example.store_management_tool.controller.dto.OrderItemUpdateRequestDto;
import com.example.store_management_tool.coverter.ProductConverter;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.enums.Role;
import com.example.store_management_tool.service.exception.OrderItemNotFoundException;
import com.example.store_management_tool.service.exception.OrderNotFoundException;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository repository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        orderItemService = new OrderItemService(repository, orderRepository,
                productRepository, userService, productConverter);
    }

    @Test
    public void addItemToOrder() {
        setUpAuthorization();

        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderId(orderId);
        orderItemDto.setProductId(productId);
        orderItemDto.setQuantity(1);
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(10.0);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        orderItemService.addItemToOrder(orderItemDto);

        Mockito.verify(repository).save(Mockito.any(OrderItem.class));
    }

    @Test
    public void addItemToOrder_throwOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderId(orderId);
        orderItemDto.setProductId(productId);
        orderItemDto.setQuantity(1);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderItemService.addItemToOrder(orderItemDto));
    }

    @Test
    public void deleteItemFromOrder() {

        setUpAuthorization();

        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);

        Mockito.when(orderRepository.existsById(orderId)).thenReturn(true);
        Mockito.when(repository.existsById(itemId)).thenReturn(true);
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        orderItemService.deleteItemFromOrder(orderId, itemId);

        Mockito.verify(repository).deleteById(itemId);
    }

    @Test
    public void deleteItemFromOrder_throwOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Mockito.when(orderRepository.existsById(orderId)).thenReturn(false);

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderItemService.deleteItemFromOrder(orderId, itemId));
    }

    @Test
    public void getItemFromOrderDetails() {
        setUpAuthorization();
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        OrderItem item = new OrderItem();
        item.setId(itemId);
        item.setOrder(order);
        Product product = new Product();
        product.setId(UUID.randomUUID());

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(repository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        OrderItemResponseDto responseDto = orderItemService.getItemFromOrderDetails(orderId, itemId);

        Assertions.assertNotNull(responseDto);
        Mockito.verify(repository).findById(itemId);
    }

    @Test
    public void updateItemInTheOrder() {
        setUpAuthorization();
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItemUpdateRequestDto updateRequestDto = new OrderItemUpdateRequestDto();
        updateRequestDto.setItemId(itemId);
        updateRequestDto.setOrderId(orderId);
        updateRequestDto.setProductId(productId);
        updateRequestDto.setQuantity(1);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");
        user.setRole(Role.ROLE_ADMIN);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        OrderItem item = new OrderItem();
        item.setId(itemId);
        item.setOrder(order);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(20.0);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(repository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);

        orderItemService.updateItemInTheOrder(updateRequestDto);

        Mockito.verify(repository).save(item);
    }

    @Test
    public void updateItemInTheOrder_throwItemNotFound() {
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItemUpdateRequestDto updateRequestDto = new OrderItemUpdateRequestDto();
        updateRequestDto.setItemId(itemId);
        updateRequestDto.setOrderId(orderId);
        updateRequestDto.setProductId(productId);
        updateRequestDto.setQuantity(1);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        Mockito.when(repository.findById(itemId)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderItemNotFoundException.class, () -> orderItemService.updateItemInTheOrder(updateRequestDto));
    }

    private void setUpAuthorization() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("user@example.com");
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
