package org.example.springapie.mappers;

import org.example.springapie.dtos.RegisterUserRequest;
import org.example.springapie.dtos.UpdateUserRequest;
import org.example.springapie.dtos.UserDto;
import org.example.springapie.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
