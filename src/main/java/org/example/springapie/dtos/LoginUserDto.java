package org.example.springapie.dtos;

import lombok.Data;

@Data
public class LoginUserDto {
    private String password;
    private String email;
}
