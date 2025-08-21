package org.example.springapie.dtos;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String name;
    private String password;
    private String email;
}
