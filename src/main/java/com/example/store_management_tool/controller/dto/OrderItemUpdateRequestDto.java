package com.example.store_management_tool.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemUpdateRequestDto {
    private UUID orderId;
    private UUID itemId;
    private UUID productId;
    private int quantity;
}
