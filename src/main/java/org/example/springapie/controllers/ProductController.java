package org.example.springapie.controllers;

import lombok.AllArgsConstructor;
import org.example.springapie.dtos.*;
import org.example.springapie.entities.Product;
import org.example.springapie.mappers.ProductMapper;
import org.example.springapie.repositories.CategoryRepository;
import org.example.springapie.repositories.ProductRepository;
import org.example.springapie.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(name = "categoryId" , required = false) Long categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {products = productRepository.findAllWithCategory();}

        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder) {
      var category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(NullPointerException::new);
//      if (category == null) {
//        return ResponseEntity.notFound().build();
//      }

      var product = productMapper.toEntity(productDto);
      product.setCategory(category);
      productRepository.save(product);
      return ResponseEntity.ok(productDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(NullPointerException::new);
//        if (category == null) {
//            return ResponseEntity.badRequest().build();
//        }

        var product = productRepository.findById(id).orElseThrow(NullPointerException::new);
//        if (product == null){
//            return ResponseEntity.notFound().build();
//        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProduct(@RequestBody SearchTextDto name,Pageable pageable) {
        Page<Product> products = productService.searchProduct(name.getName(), pageable);
        return ResponseEntity.ok(products.map(productMapper::toDto));
    }

}
