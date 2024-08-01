package com.example.store_management_tool.controller.dto;

import com.example.store_management_tool.service.enums.PaymentMethod;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderDto {

    private List<OrderItemDto> itemDtoList;
    private PaymentMethod paymentMethod;
}
