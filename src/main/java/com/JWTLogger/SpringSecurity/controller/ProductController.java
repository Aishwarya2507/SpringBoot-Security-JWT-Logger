package com.JWTLogger.SpringSecurity.controller;


import com.JWTLogger.SpringSecurity.model.Product;
import com.JWTLogger.SpringSecurity.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        logger.info("Creating product: {}", product.getName());
        return productRepository.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        logger.info("Fetching product by ID: {}", id);
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Updating product with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        return productRepository.save(existingProduct);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }

}
