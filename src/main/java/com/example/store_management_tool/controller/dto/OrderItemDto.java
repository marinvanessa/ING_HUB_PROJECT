package com.example.store_management_tool.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemDto {

    private UUID orderId;
    private UUID productId;
    private int quantity;
}
