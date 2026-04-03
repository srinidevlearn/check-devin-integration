package com.srinidevlearn.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srinidevlearn.productservice.model.Product;
import com.srinidevlearn.productservice.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/products should return all products with 200 OK")
    void getAllProducts_shouldReturnAllProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product("1", "Laptop", "High-performance laptop", new BigDecimal("999.99"), "Electronics"),
                new Product("2", "Headphones", "Wireless headphones", new BigDecimal("199.99"), "Electronics")
        );
        when(productService.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Headphones"));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return product when found")
    void getProductById_shouldReturnProduct_whenFound() throws Exception {
        // Arrange
        Product product = new Product("1", "Laptop", "High-performance laptop",
                new BigDecimal("999.99"), "Electronics");
        when(productService.getProductById("1")).thenReturn(Optional.of(product));

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 404 when not found")
    void getProductById_shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        when(productService.getProductById("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/products/category/{category} should return matching products")
    void getProductsByCategory_shouldReturnMatchingProducts() throws Exception {
        // Arrange
        List<Product> electronics = Arrays.asList(
                new Product("1", "Laptop", "High-performance laptop", new BigDecimal("999.99"), "Electronics")
        );
        when(productService.getProductsByCategory("Electronics")).thenReturn(electronics);

        // Act & Assert
        mockMvc.perform(get("/api/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("Electronics"));
    }

    @Test
    @DisplayName("GET /api/products/category/{category} should return empty list when no match")
    void getProductsByCategory_shouldReturnEmptyList_whenNoMatch() throws Exception {
        // Arrange
        when(productService.getProductsByCategory("NonExistent")).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/products/category/NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("POST /api/products should create product and return 201")
    void createProduct_shouldCreateAndReturn201() throws Exception {
        // Arrange
        Product inputProduct = new Product(null, "Tablet", "10-inch tablet",
                new BigDecimal("499.99"), "Electronics");
        Product savedProduct = new Product("5", "Tablet", "10-inch tablet",
                new BigDecimal("499.99"), "Electronics");
        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.name").value("Tablet"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should return 204 when product exists")
    void deleteProduct_shouldReturn204_whenExists() throws Exception {
        // Arrange
        Product product = new Product("1", "Laptop", "High-performance laptop",
                new BigDecimal("999.99"), "Electronics");
        when(productService.deleteProduct("1")).thenReturn(Optional.of(product));

        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should return 404 when not found")
    void deleteProduct_shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        when(productService.deleteProduct("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound());
    }
}
