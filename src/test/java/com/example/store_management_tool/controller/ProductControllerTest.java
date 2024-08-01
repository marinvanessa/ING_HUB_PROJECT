package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.ProductDto;
import com.example.store_management_tool.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setId(UUID.randomUUID());
        productDto.setName("milk");
        productDto.setDescription("for children");
        productDto.setPrice(10.34);
        ObjectMapper objectMapper = new ObjectMapper();
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Product added successfully!"));

        Mockito.verify(productService).addProduct(Mockito.any(ProductDto.class));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProductPrice() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/admin/products")
                        .param("id", productId.toString())
                        .param("newPrice", "150.0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product updated successfully!"));

        Mockito.verify(productService).updateProductPrice(Mockito.any(UUID.class), Mockito.anyDouble());

    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Products deleted successfully!"));

        Mockito.verify(productService).deleteAllProducts();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteProduct() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product deleted successfully!"));

        Mockito.verify(productService).deleteProductById(Mockito.any(UUID.class));

    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetProduct() throws Exception {
        UUID productId = UUID.randomUUID();

        ProductDto productDto = new ProductDto();
        productDto.setId(UUID.randomUUID());
        productDto.setName("milk");
        productDto.setDescription("for children");
        productDto.setPrice(10.34);
        Mockito.when(productService.getProductById(Mockito.any(UUID.class))).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("milk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10.34));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetProducts() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setId(UUID.randomUUID());
        productDto.setName("milk");
        productDto.setDescription("for children");
        productDto.setPrice(10.34);
        Mockito.when(productService.getProducts()).thenReturn(Collections.singletonList(productDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("milk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(10.34));
    }
}
