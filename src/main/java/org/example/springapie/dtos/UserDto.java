package org.example.springapie.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.springapie.entities.ShoppingCart;

@AllArgsConstructor
@Getter
public class UserDto {
    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private ShoppingCart shoppingCart;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdAt;
}
