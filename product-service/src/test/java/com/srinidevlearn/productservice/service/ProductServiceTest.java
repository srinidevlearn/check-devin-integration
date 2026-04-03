package com.srinidevlearn.productservice.service;

import com.srinidevlearn.productservice.event.ProductEventPublisher;
import com.srinidevlearn.productservice.model.Product;
import com.srinidevlearn.productservice.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductEventPublisher productEventPublisher;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("getAllProducts should return all products from repository")
    void getAllProducts_shouldReturnAllProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(
                new Product("1", "Laptop", "High-performance laptop", new BigDecimal("999.99"), "Electronics"),
                new Product("2", "Headphones", "Wireless headphones", new BigDecimal("199.99"), "Electronics")
        );
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getAllProducts();

        // Assert
        assertEquals(2, actualProducts.size());
        assertEquals("Laptop", actualProducts.get(0).getName());
        assertEquals("Headphones", actualProducts.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getProductById should return product when it exists")
    void getProductById_shouldReturnProduct_whenExists() {
        // Arrange
        Product expectedProduct = new Product("1", "Laptop", "High-performance laptop",
                new BigDecimal("999.99"), "Electronics");
        when(productRepository.findById("1")).thenReturn(Optional.of(expectedProduct));

        // Act
        Optional<Product> actualProduct = productService.getProductById("1");

        // Assert
        assertTrue(actualProduct.isPresent());
        assertEquals("Laptop", actualProduct.get().getName());
        assertEquals(new BigDecimal("999.99"), actualProduct.get().getPrice());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("getProductById should return empty when product does not exist")
    void getProductById_shouldReturnEmpty_whenDoesNotExist() {
        // Arrange
        when(productRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<Product> actualProduct = productService.getProductById("999");

        // Assert
        assertFalse(actualProduct.isPresent());
        verify(productRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("getProductsByCategory should return matching products")
    void getProductsByCategory_shouldReturnMatchingProducts() {
        // Arrange
        List<Product> electronics = Arrays.asList(
                new Product("1", "Laptop", "High-performance laptop", new BigDecimal("999.99"), "Electronics"),
                new Product("2", "Headphones", "Wireless headphones", new BigDecimal("199.99"), "Electronics")
        );
        when(productRepository.findByCategory("Electronics")).thenReturn(electronics);

        // Act
        List<Product> result = productService.getProductsByCategory("Electronics");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> "Electronics".equals(p.getCategory())));
        verify(productRepository, times(1)).findByCategory("Electronics");
    }

    @Test
    @DisplayName("createProduct should save product and publish event")
    void createProduct_shouldSaveAndPublishEvent() {
        // Arrange
        Product inputProduct = new Product(null, "Tablet", "10-inch tablet",
                new BigDecimal("499.99"), "Electronics");
        Product savedProduct = new Product("5", "Tablet", "10-inch tablet",
                new BigDecimal("499.99"), "Electronics");
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(inputProduct);

        // Assert
        assertNotNull(result);
        assertEquals("5", result.getId());
        assertEquals("Tablet", result.getName());
        verify(productRepository, times(1)).save(inputProduct);
        verify(productEventPublisher, times(1))
                .publishProductCreated("5", "Tablet", "Electronics");
    }

    @Test
    @DisplayName("deleteProduct should delete and publish event when product exists")
    void deleteProduct_shouldDeleteAndPublishEvent_whenExists() {
        // Arrange
        Product existingProduct = new Product("1", "Laptop", "High-performance laptop",
                new BigDecimal("999.99"), "Electronics");
        when(productRepository.findById("1")).thenReturn(Optional.of(existingProduct));

        // Act
        Optional<Product> result = productService.deleteProduct("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
        verify(productRepository, times(1)).deleteById("1");
        verify(productEventPublisher, times(1))
                .publishProductDeleted("1", "Laptop", "Electronics");
    }

    @Test
    @DisplayName("deleteProduct should return empty and not publish when product does not exist")
    void deleteProduct_shouldReturnEmpty_whenDoesNotExist() {
        // Arrange
        when(productRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.deleteProduct("999");

        // Assert
        assertFalse(result.isPresent());
        verify(productRepository, never()).deleteById(any());
        verify(productEventPublisher, never()).publishProductDeleted(any(), any(), any());
    }
}
