package com.example.store_management_tool.controller.dto;

import com.example.store_management_tool.service.enums.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Setter
public class OrderResponseDto {

    private UUID id;
    private double totalPrice;
    private PaymentMethod paymentMethod;
}
