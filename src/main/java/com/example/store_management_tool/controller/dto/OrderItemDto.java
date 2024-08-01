package com.example.store_management_tool.controller.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderItemDto {

    private UUID productId;
    private int quantity;
    private double price;
}
