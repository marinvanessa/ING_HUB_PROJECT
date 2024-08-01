package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.exception.OrderNotFoundException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import com.example.store_management_tool.service.model.Order;
import com.example.store_management_tool.service.model.OrderItem;
import com.example.store_management_tool.service.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository repository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addItemToOrder(OrderItemDto orderItemDto) {
        Order order = orderRepository.findById(orderItemDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
        repository.save(orderItem);
    }

}
