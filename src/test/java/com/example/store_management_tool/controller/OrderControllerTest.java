package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.OrderDto;
import com.example.store_management_tool.controller.dto.OrderResponseDto;
import com.example.store_management_tool.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getOrders() throws Exception {

        List<OrderResponseDto> orders = List.of(new OrderResponseDto(), new OrderResponseDto());
        Mockito.when(orderService.getOrders()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .header("Authorization", "Bearer some-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists());
    }

    @Test
    public void deleteOrderById() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{id}", orderId)
                        .header("Authorization", "Bearer some-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order deleted successfully!"));

        Mockito.verify(orderService).deleteOrderById(orderId);
    }
    @Test
    public void getOrderDetails() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        Mockito.when(orderService.getOrderDetails(orderId)).thenReturn(orderResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    public void placeOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        String orderDtoJson = new ObjectMapper().writeValueAsString(orderDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .header("Authorization", "Bearer some-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order placed successfully!"));

        Mockito.verify(orderService).placeOrder(Mockito.any(OrderDto.class));
    }

    @Test
    public void getOrdersByUser() throws Exception {
        UUID userId = UUID.randomUUID();
        List<OrderResponseDto> orders = List.of(new OrderResponseDto(), new OrderResponseDto());
        Mockito.when(orderService.getOrdersByUser(userId)).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user/{userId}", userId)
                        .header("Authorization", "Bearer some-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists());
    }

}
