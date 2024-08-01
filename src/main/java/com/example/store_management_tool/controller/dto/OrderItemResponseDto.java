package com.example.store_management_tool.controller.dto;


import com.example.store_management_tool.service.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemResponseDto {

    private Product product;
    private int quantity;
}
