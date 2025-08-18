package com.nexora.easeshop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexora.easeshop.exceptions.ProductAlreadyInCartException;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems= new ArrayList<>();

    @OneToOne(mappedBy = "cart")
    @JsonIgnore
    private Customer customer;

    public void removeProduct(Long productId) {
        CartItem item = cartItems.stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Le produit n'est pas dans le panier"));

        cartItems.remove(item);
        item.setCart(null);
    }

    public void addProduct(Product product) {
        cartItems.stream()
                .map(CartItem::getProduct)
                .map(Product::getId)
                .filter(id -> id.equals(product.getId()))
                .findAny()
                .ifPresent(p -> {
                    throw new ProductAlreadyInCartException("Le produit est déjà dans le panier");
                });

        CartItem item = new CartItem();
        item.setQuantity(1);
        item.setProduct(product);
        item.setCart(this);
        cartItems.add(item);
    }

    public BigDecimal getTotalAmount() {
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
