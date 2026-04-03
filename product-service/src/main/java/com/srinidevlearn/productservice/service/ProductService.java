package com.srinidevlearn.productservice.service;

import com.srinidevlearn.productservice.model.Product;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init() {
        products.add(new Product(1L, "Laptop", "High-performance laptop", new BigDecimal("999.99"), "Electronics"));
        products.add(new Product(2L, "Headphones", "Wireless noise-cancelling headphones", new BigDecimal("199.99"), "Electronics"));
        products.add(new Product(3L, "Coffee Maker", "Automatic drip coffee maker", new BigDecimal("49.99"), "Kitchen"));
        products.add(new Product(4L, "Running Shoes", "Lightweight running shoes", new BigDecimal("129.99"), "Sports"));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public List<Product> getProductsByCategory(String category) {
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public Product createProduct(Product product) {
        Long nextId = products.stream()
                .mapToLong(Product::getId)
                .max()
                .orElse(0L) + 1;
        product.setId(nextId);
        products.add(product);
        return product;
    }

    public Optional<Product> deleteProduct(Long id) {
        Optional<Product> product = getProductById(id);
        product.ifPresent(products::remove);
        return product;
    }
}
