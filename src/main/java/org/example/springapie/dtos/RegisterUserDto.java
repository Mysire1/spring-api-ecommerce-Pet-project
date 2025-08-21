package org.example.springapie.dtos;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String name;
    private String password;
    private String email;
}
