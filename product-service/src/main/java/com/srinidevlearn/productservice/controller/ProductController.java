package com.srinidevlearn.productservice.controller;

import com.srinidevlearn.productservice.model.Product;
import com.srinidevlearn.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "CRUD operations for products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all available products")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "Product ID", required = true) @PathVariable String id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieves all products in a specific category")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products by category")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Product category", required = true) @PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product and publishes a creation event")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product object to create", required = true) @RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product by ID and publishes a deletion event")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID to delete", required = true) @PathVariable String id) {
        return productService.deleteProduct(id)
                .map(p -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
