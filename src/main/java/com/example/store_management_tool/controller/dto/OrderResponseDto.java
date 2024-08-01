package com.example.store_management_tool.controller.dto;

import com.example.store_management_tool.service.enums.PaymentMethod;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
public class OrderResponseDto {

    private UUID id;
    private double totalPrice;
    private PaymentMethod paymentMethod;
    private List<OrderItemResponseDto> orderItemResponseDtoList;
}
