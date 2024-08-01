package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.controller.dto.UpdatePriceProductDto;
import com.example.store_management_tool.service.ProductService;
import com.example.store_management_tool.service.exception.ProductAlreadyExistsException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import com.example.store_management_tool.service.model.Product;
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
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto) throws ProductAlreadyExistsException {
        productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!");
    }

    @PutMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProductPrice(@RequestBody UpdatePriceProductDto updatePriceProductDto) throws ProductNotFoundException {
        productService.updateProductPrice(updatePriceProductDto);
        return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully!");
    }

    @DeleteMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body("Products deleted successfully!");
    }

    @DeleteMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) throws ProductNotFoundException {
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully!");
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID id) throws ProductNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }
}
