package org.example.springapie.mappers;

import javax.annotation.processing.Generated;
import org.example.springapie.dtos.RegisterUserRequest;
import org.example.springapie.dtos.UpdateUserRequest;
import org.example.springapie.dtos.UserDto;
import org.example.springapie.entities.ShoppingCart;
import org.example.springapie.entities.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-21T19:47:39+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;
        String password = null;
        ShoppingCart shoppingCart = null;

        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();
        shoppingCart = user.getShoppingCart();

        UserDto userDto = new UserDto( id, name, email, password, shoppingCart );

        return userDto;
    }

    @Override
    public User toEntity(RegisterUserRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( request.getName() );
        user.email( request.getEmail() );
        user.password( request.getPassword() );

        return user.build();
    }

    @Override
    public void update(UpdateUserRequest request, User user) {
        if ( request == null ) {
            return;
        }

        user.setName( request.getName() );
        user.setEmail( request.getEmail() );
    }
}
