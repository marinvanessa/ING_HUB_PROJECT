package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.service.ProductService;
import com.example.store_management_tool.service.exception.ProductAlreadyExistsException;
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
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto) throws ProductAlreadyExistsException {
        productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!");
    }
}
