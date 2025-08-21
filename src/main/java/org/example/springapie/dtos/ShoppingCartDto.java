package org.example.springapie.dtos;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ShoppingCartDto {
    @Id
    private Long id;
}
