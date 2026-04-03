package com.srinidevlearn.productservice.service;

import com.srinidevlearn.productservice.event.ProductEventPublisher;
import com.srinidevlearn.productservice.model.Product;
import com.srinidevlearn.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductEventPublisher productEventPublisher;

    public ProductService(ProductRepository productRepository, ProductEventPublisher productEventPublisher) {
        this.productRepository = productRepository;
        this.productEventPublisher = productEventPublisher;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product createProduct(Product product) {
        Product saved = productRepository.save(product);
        productEventPublisher.publishProductCreated(saved.getId(), saved.getName(), saved.getCategory());
        return saved;
    }

    public Optional<Product> deleteProduct(String id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(p -> {
            productRepository.deleteById(id);
            productEventPublisher.publishProductDeleted(p.getId(), p.getName(), p.getCategory());
        });
        return product;
    }
}
