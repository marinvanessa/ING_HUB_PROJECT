//package com.example.store_management_tool.controller;
//
//import com.example.store_management_tool.controller.dto.ProductDto;
//import com.example.store_management_tool.service.ProductService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class ProductControllerTest {
//    private MockMvc mockMvc;
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private ProductController productController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void addProduct_shouldReturnCreatedStatus() throws Exception {
//        ProductDto productDto = new ProductDto();
//        ObjectMapper objectMapper = new ObjectMapper();
//        String productDtoJson = objectMapper.writeValueAsString(productDto);
//
//        mockMvc.perform(post("/api/admin/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(productDtoJson))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("Product added successfully!"));
//
//        verify(productService).addProduct(any(ProductDto.class));
//    }
//}
