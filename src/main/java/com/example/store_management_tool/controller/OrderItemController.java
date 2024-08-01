package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.service.OrderItemService;
import com.example.store_management_tool.service.exception.OrderNotFoundException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
