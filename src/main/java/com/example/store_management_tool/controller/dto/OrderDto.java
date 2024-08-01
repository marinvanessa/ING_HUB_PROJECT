package com.example.store_management_tool.controller.dto;

import com.example.store_management_tool.service.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {

    private List<OrderItemDto> itemDtoList;
    private PaymentMethod paymentMethod;
}
