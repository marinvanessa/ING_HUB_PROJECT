package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.OrderDto;
import com.example.store_management_tool.controller.dto.OrderResponseDto;
import com.example.store_management_tool.service.OrderService;
import com.example.store_management_tool.service.exception.OrderNotFoundException;
import com.example.store_management_tool.service.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrders());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrderById(@PathVariable UUID id) throws OrderNotFoundException {
        orderService.deleteOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully!");
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OrderResponseDto> getOrderDetails(@PathVariable UUID id) throws OrderNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderDetails(id));
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> placeOrder(@RequestBody OrderDto orderDto) throws OrderNotFoundException {
        orderService.placeOrder(orderDto);
        return ResponseEntity.status(HttpStatus.OK).body("Order placed successfully!");
    }

    @GetMapping("/orders/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@PathVariable UUID userId) throws OrderNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByUser(userId));
    }

}
