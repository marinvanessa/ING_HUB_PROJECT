package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.controller.dto.OrderItemResponseDto;
import com.example.store_management_tool.controller.dto.OrderItemUpdateRequestDto;
import com.example.store_management_tool.service.OrderItemService;
import com.example.store_management_tool.service.exception.OrderItemNotFoundException;
import com.example.store_management_tool.service.exception.OrderNotFoundException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService service;

    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> addItemToOrder(@RequestBody OrderItemDto orderItemDto) throws
            OrderNotFoundException, ProductNotFoundException {
        service.addItemToOrder(orderItemDto);
        return ResponseEntity.status(HttpStatus.OK).body("Item added successfully!");
    }

    @DeleteMapping("/orders/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> addItemToOrder(@PathVariable UUID orderId, @PathVariable UUID itemId) throws
            OrderNotFoundException, ProductNotFoundException {
        service.deleteItemFromOrder(orderId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body("Item deleted successfully!");
    }

    @GetMapping("/orders/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OrderItemResponseDto> getItemDetails(@PathVariable UUID orderId, @PathVariable UUID itemId)
            throws OrderNotFoundException, OrderItemNotFoundException {
        OrderItemResponseDto orderItemResponseDto = service.getItemFromOrderDetails(orderId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(orderItemResponseDto);
    }


    @PutMapping("/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> updateItemInTheOrder(@RequestBody OrderItemUpdateRequestDto updateRequestDto)
            throws OrderNotFoundException, OrderItemNotFoundException {
        service.updateItemInTheOrder(updateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body("Item updated successfully");
    }
}
