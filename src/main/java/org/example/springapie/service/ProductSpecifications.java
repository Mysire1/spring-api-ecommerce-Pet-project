package org.example.springapie.service;

import org.example.springapie.entities.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductSpecifications {
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(name)) { // Перевіряємо, чи є текст у назві
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // Якщо ні, повертаємо true
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
