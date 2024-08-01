package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.coverter.ProductConverter;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.exception.ProductAlreadyExistsException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import com.example.store_management_tool.service.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductConverter productConvertor;

    @Transactional
    public void addProduct(ProductDto productDto) {
        Product product = repository.findById(productDto.getId()).orElse(null);

        if (product != null) {
            throw new ProductAlreadyExistsException(product.getId().toString());
        }

        repository.save(productConvertor.convertDtoToEntity(productDto));
    }

    @Transactional
    public void updateProductPrice(UUID id, double newPrice) {
        Product oldProduct = repository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(id.toString()));

        oldProduct.setPrice(newPrice);
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

    public ProductDto getProductById(UUID id) {
        return productConvertor.convertEntityToDto(repository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException(id.toString())));
    }

    public List<ProductDto> getProducts() {
        return repository.findAll().stream().map(productConvertor::convertEntityToDto)
                .collect(Collectors.toList());
    }
}