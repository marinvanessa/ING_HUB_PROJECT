package com.example.store_management_tool.coverter;

import com.example.store_management_tool.controller.dto.OrderItemResponseDto;
import com.example.store_management_tool.service.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemConverter {
    private final ProductConverter productConverter;

    public OrderItemResponseDto convertEntityToOrderItemResponseDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setQuantity(orderItem.getQuantity());
        orderItemResponseDto.setProductDto(productConverter.convertEntityToDto(orderItem.getProduct()));
        return orderItemResponseDto;
    }
}
