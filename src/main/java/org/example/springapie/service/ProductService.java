package org.example.springapie.service;

import lombok.AllArgsConstructor;
import org.example.springapie.entities.Product;
import org.example.springapie.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> searchProduct(String searchText,Pageable pageable) {
        Specification<Product> specification = ProductSpecifications.hasName(searchText);
        return productRepository.findAll(specification, pageable);
    }


    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));
    }
}
