package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.controller.dto.OrderItemResponseDto;
import com.example.store_management_tool.controller.dto.OrderItemUpdateRequestDto;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.exception.OrderItemNotFoundException;
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

    @Transactional
    public void deleteItemFromOrder(UUID orderId, UUID itemId) {

        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("order not found");

        }

        if (!repository.existsById(itemId)) {
            throw new OrderItemNotFoundException("item not found");

        }

        repository.deleteById(itemId);
    }
    public OrderItemResponseDto getItemFromOrderDetails(UUID orderId, UUID itemId) {
        Order order =  orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order not found"));
        OrderItem item = repository.findById(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException("Item not found"));

        if (!item.getOrder().equals(order)) {
//             TODO:
            System.out.println("add an exception here");
        }


        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setProduct(item.getProduct());
        orderItemResponseDto.setQuantity(item.getQuantity());
        return orderItemResponseDto;
    }

    @Transactional
    public void updateItemInTheOrder(OrderItemUpdateRequestDto updateRequestDto) {
        Order order =  orderRepository.findById(updateRequestDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("order not found"));
        OrderItem item = repository.findById(updateRequestDto.getItemId())
                .orElseThrow(() -> new OrderItemNotFoundException("Item not found"));
        Product product = productRepository.findById(updateRequestDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));


        if (!item.getOrder().equals(order)) {
//             TODO:
            System.out.println("add an exception here");
        }

        item.setProduct(product);
        item.setQuantity(updateRequestDto.getQuantity());
        item.setPrice(updateRequestDto.getQuantity() * product.getPrice());

        repository.save(item);

    }


}
