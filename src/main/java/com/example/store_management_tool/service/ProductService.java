package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.controller.dto.UpdatePriceProductDto;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.exception.ProductAlreadyExistsException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import com.example.store_management_tool.service.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional
    public void addProduct(ProductDto productDto) {
        Product product = repository.findByName(productDto.getName()).orElse(null);

        if (product != null) {
            throw new ProductAlreadyExistsException(product.getId().toString());
        }

        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID());
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setPrice(productDto.getPrice());
        repository.save(newProduct);
    }

    @Transactional
    public void updateProductPrice(UpdatePriceProductDto updatePriceProductDto) {
        Product oldProduct = repository.findByName(updatePriceProductDto.getName()).orElseThrow(() ->
                new ProductNotFoundException("gggg"));

        oldProduct.setPrice(updatePriceProductDto.getPrice());
        repository.save(oldProduct);
    }

    @Transactional
    public void deleteAllProducts() {
        repository.deleteAll();
    }

    @Transactional
    public void deleteProductById(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ProductNotFoundException(id.toString());
        }
    }

    public Product getProductById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id.toString()));
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }
}