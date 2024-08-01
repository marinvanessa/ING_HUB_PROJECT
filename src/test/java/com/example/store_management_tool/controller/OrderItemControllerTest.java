package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.controller.dto.OrderItemResponseDto;
import com.example.store_management_tool.controller.dto.OrderItemUpdateRequestDto;
import com.example.store_management_tool.service.OrderItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class OrderItemControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderItemController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testAddItemToOrder() throws Exception {
        OrderItemDto orderItemDto = new OrderItemDto();
        String orderItemDtoJson = new ObjectMapper().writeValueAsString(orderItemDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items")
                        .header("Authorization", "Bearer some-token") // Simulează autentificarea
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderItemDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Item added successfully!"));

        Mockito.verify(orderItemService).addItemToOrder(Mockito.any(OrderItemDto.class));
    }

    @Test
    public void testDeleteItemFromOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{orderId}/items/{itemId}", orderId, itemId)
                        .header("Authorization", "Bearer some-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Item deleted successfully!"));

        Mockito.verify(orderItemService).deleteItemFromOrder(orderId, itemId);
    }

    @Test
    public void testGetItemDetails() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        Mockito.when(orderItemService.getItemFromOrderDetails(orderId, itemId)).thenReturn(orderItemResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{orderId}/items/{itemId}", orderId, itemId)
                        .header("Authorization", "Bearer some-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());

        Mockito.verify(orderItemService).getItemFromOrderDetails(orderId, itemId);
    }

    @Test
    public void testUpdateItemInTheOrder() throws Exception {
        OrderItemUpdateRequestDto updateRequestDto = new OrderItemUpdateRequestDto();
        String updateRequestDtoJson = new ObjectMapper().writeValueAsString(updateRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/items")
                        .header("Authorization", "Bearer some-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Item updated successfully"));

       Mockito.verify(orderItemService).updateItemInTheOrder(Mockito.any(OrderItemUpdateRequestDto.class));
    }
}
