package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.exception.ProductAlreadyExistsException;
import com.example.store_management_tool.service.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional
    public void addProduct(ProductDto productDto) {
        Product product = repository.findByName(productDto.getName()).orElse(null);

        if (product != null) {
            throw new ProductAlreadyExistsException("Product with name '" + product.getName() + "' already exists");
        }

        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID());
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setPrice(productDto.getPrice());
        repository.save(newProduct);
    }
}
