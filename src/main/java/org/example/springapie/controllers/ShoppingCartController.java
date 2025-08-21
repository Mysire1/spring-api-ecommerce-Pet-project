package org.example.springapie.controllers;

import lombok.AllArgsConstructor;
import org.example.springapie.entities.ShoppingCart;
import org.example.springapie.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping("/{userId}/addItem")
    public ResponseEntity<Void> addItem (@PathVariable Long userId,
                                         @RequestParam Long productId ,
                                         @RequestParam int quantity) {
        shoppingCartService.addCartItem(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Void> removeItem (@PathVariable Long userId,
                                            @RequestParam Long productId) {
        shoppingCartService.removeCartItem(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        shoppingCartService.clearCartItem(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingCartService.getCartForUser(userId));
    }

}
