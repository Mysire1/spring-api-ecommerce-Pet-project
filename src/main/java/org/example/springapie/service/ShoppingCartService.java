package org.example.springapie.service;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.springapie.dtos.ProductDto;
import org.example.springapie.entities.CartItem;
import org.example.springapie.entities.Product;
import org.example.springapie.entities.ShoppingCart;
import org.example.springapie.entities.User;
import org.example.springapie.repositories.ProductRepository;
import org.example.springapie.repositories.ShoppingCartRepository;
import org.example.springapie.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShoppingCartService {
    private ProductService productService;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart getCartForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        if (user.getShoppingCart() == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);
            user.setShoppingCart(shoppingCart);
        }
        return user.getShoppingCart();
    }

    public void addCartItem(Long userId, Long cartItemId, int quantity) {
        ShoppingCart shoppingCart = getCartForUser(userId);
        Product product = productRepository.findById(cartItemId).orElseThrow();

        Optional<CartItem> existingItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId)).findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            shoppingCart.getCartItems().add(cartItem);
        }
        shoppingCartRepository.save(shoppingCart);
    }

    public void removeCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getCartForUser(userId);
        shoppingCart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));
        shoppingCartRepository.save(shoppingCart);
    }

    public void clearCartItem(Long userId) {
        ShoppingCart shoppingCart = getCartForUser(userId);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }

}
