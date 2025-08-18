package com.nexora.easeshop.controllers;

import com.nexora.easeshop.dtos.CartItem.CartItemDto;
import com.nexora.easeshop.models.CartItem;
import com.nexora.easeshop.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;


import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Ajouter un produit
    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId) {
        cartService.addToCart(productId);
        return ResponseEntity.ok("Produit ajouté au panier");
    }

    // Afficher le contenu du panier
    @GetMapping()
    public ResponseEntity<List<CartItemDto>> getCart() {
        List<CartItem> cartItems = cartService.getCustomerCartItems();

        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(CartItemDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cartItemDtos);
    }

    // Supprimer un produit du panier
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.ok("Produit retiré du panier");
    }

    // Retourner le nombre d'éléments du panier courant
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCartItemsCount() {
        int countItem = cartService.getCurrentCartItemsCount();
        return ResponseEntity.ok(Map.of("countItem", countItem));
    }
}
