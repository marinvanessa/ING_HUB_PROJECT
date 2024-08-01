package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.OrderItemDto;
import com.example.store_management_tool.controller.dto.OrderItemResponseDto;
import com.example.store_management_tool.controller.dto.OrderItemUpdateRequestDto;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.OrderRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.exception.ItemNotFoundInsideOrderException;
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
        Order order = getOrder(orderItemDto.getOrderId());
        Product product = getProduct(orderItemDto.getProductId());

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
            throw new OrderNotFoundException(orderId.toString());

        }

        if (!repository.existsById(itemId)) {
            throw new OrderItemNotFoundException(itemId.toString());

        }

        repository.deleteById(itemId);
    }

    public OrderItemResponseDto getItemFromOrderDetails(UUID orderId, UUID itemId) {
        Order order = getOrder(orderId);
        OrderItem item = getOrderItem(itemId);

        if (!item.getOrder().equals(order)) {
            throw new ItemNotFoundInsideOrderException(itemId.toString());
        }


        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setProduct(item.getProduct());
        orderItemResponseDto.setQuantity(item.getQuantity());
        return orderItemResponseDto;
    }

    @Transactional
    public void updateItemInTheOrder(OrderItemUpdateRequestDto updateRequestDto) {
        Order order = getOrder(updateRequestDto.getOrderId());
        OrderItem item = getOrderItem(updateRequestDto.getItemId());
        Product product = getProduct(updateRequestDto.getProductId());


        if (!item.getOrder().equals(order)) {
            throw new ItemNotFoundInsideOrderException(item.getId().toString());
        }

        item.setProduct(product);
        item.setQuantity(updateRequestDto.getQuantity());
        item.setPrice(updateRequestDto.getQuantity() * product.getPrice());

        repository.save(item);

    }

    private OrderItem getOrderItem(UUID itemId) {
        return repository.findById(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException(itemId.toString()));
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
    }
}
