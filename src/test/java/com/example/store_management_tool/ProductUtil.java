package com.example.store_management_tool;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.service.model.Product;

import java.util.UUID;

public class ProductUtil {

    public static Product createProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("product");
        product.setDescription("best product that we have");
        product.setPrice(12L);
        return product;

    }

    public static ProductDto createProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(UUID.randomUUID());
        productDto.setName("product");
        productDto.setDescription("best product that we have");
        productDto.setPrice(12L);
        return productDto;

    }
}
