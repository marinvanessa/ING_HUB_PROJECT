package com.example.store_management_tool.coverter;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.service.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    public Product convertDtoToEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        return product;
    }

    public ProductDto convertEntityToDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        return productDto;
    }
}
