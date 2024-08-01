package com.example.store_management_tool.service;

import com.example.store_management_tool.ProductUtil;
import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.coverter.ProductConverter;
import com.example.store_management_tool.repository.OrderItemRepository;
import com.example.store_management_tool.repository.ProductRepository;
import com.example.store_management_tool.service.exception.ProductAlreadyExistsException;
import com.example.store_management_tool.service.exception.ProductNotFoundException;
import com.example.store_management_tool.service.model.OrderItem;
import com.example.store_management_tool.service.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductConverter productConverter;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductService(productRepository, productConverter,orderItemRepository);
    }

    @Test
    void addProduct() {
        UUID productId = UUID.randomUUID();
        ProductDto productDto = ProductUtil.createProductDto();
        productDto.setId(productId);
        Product product = ProductUtil.createProduct();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(productConverter.convertDtoToEntity(productDto)).thenReturn(product);

        productService.addProduct(productDto);

        verify(productRepository).save(Mockito.any(Product.class));
    }

    @Test
    void addProduct_throwsExceptionIfProductExists() {
        UUID productId = UUID.randomUUID();
        ProductDto productDto = new ProductDto();
        productDto.setId(productId);

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Assertions.assertThrows(ProductAlreadyExistsException.class, () ->
                productService.addProduct(productDto));

    }

    @Test
    void updateProductPrice() {
        UUID productId = UUID.randomUUID();
        double newPrice = 10.89;
        Product existingProduct = ProductUtil.createProduct();
        existingProduct.setId(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(existingProduct);
        orderItem.setQuantity(2);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(orderItemRepository.findAllByProductId(productId)).thenReturn(Collections.singletonList(orderItem));

        productService.updateProductPrice(productId, newPrice);

        verify(productRepository).save(existingProduct);
        verify(orderItemRepository).save(orderItem);
        assertEquals(newPrice, existingProduct.getPrice());
        assertEquals(newPrice * orderItem.getQuantity(), orderItem.getPrice());
    }

    @Test
    void deleteAllProducts() {
        productService.deleteAllProducts();
        verify(productRepository).deleteAll();
    }


    @Test
    void deleteProductById_throwExceptionIfProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.existsById(productId)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () ->
                productService.deleteProductById(productId));
        Mockito.verify(productRepository, never()).deleteById(productId);
    }

    @Test
    void deleteProductById() {
        UUID productId = UUID.randomUUID();
        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProductById(productId);

        verify(productRepository).deleteById(productId);
    }
    @Test
    void getProductById() {
        UUID productId = UUID.randomUUID();
        Product product = ProductUtil.createProduct();
        product.setId(productId);

        ProductDto productDto = ProductUtil.createProductDto();
        productDto.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productConverter.convertEntityToDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(productId);

        assertEquals(productDto, result);
    }

    @Test
    void getProducts() {
        Product milkProduct = ProductUtil.createProduct();
        Product veganProduct = ProductUtil.createProduct();

        ProductDto milkProductDto = ProductUtil.createProductDto();
        ProductDto veganProductDto = ProductUtil.createProductDto();

        when(productRepository.findAll()).thenReturn(List.of(milkProduct, veganProduct));
        when(productConverter.convertEntityToDto(milkProduct)).thenReturn(milkProductDto);
        when(productConverter.convertEntityToDto(veganProduct)).thenReturn(veganProductDto);

        List<ProductDto> result = productService.getProducts();

        assertEquals(2, result.size());
        assertTrue(result.contains(milkProductDto));
        assertTrue(result.contains(veganProductDto));
    }

}
