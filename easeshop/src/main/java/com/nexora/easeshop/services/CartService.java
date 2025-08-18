package com.nexora.easeshop.services;

import com.nexora.easeshop.models.Cart;
import com.nexora.easeshop.models.CartItem;
import com.nexora.easeshop.models.Product;
import com.nexora.easeshop.repositories.CartItemRepository;
import com.nexora.easeshop.repositories.CartRepository;
import com.nexora.easeshop.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CustomerService customerService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    public void addToCart(Long productId) {
        Product product = productService.getProductById(productId);
        Cart cart = getCustomerCart();

        cart.addProduct(product);
        cartRepository.save(cart);
    }

    // Récupérer le panier de l'utilisateur courant
    public Cart getCustomerCart() {
        return authenticationService.getCurrentUser().getCart();
    }

    //recuperer le contenu du panier de l'utilisateur courant
    public List<CartItem> getCustomerCartItems() {
        return getCustomerCart().getCartItems();
    }

    // Retirer un produit du panier de l'utilisateur courant
    public void removeFromCart(Long productId) {
        Cart cart = getCustomerCart();
        cart.removeProduct(productId);
        cartRepository.save(cart);
    }

    // avoir le nombre d'éléments du panier courant
    public int getCurrentCartItemsCount() {
        Cart cart = getCustomerCart();
        return cart.getCartItems().size();
    }

}
