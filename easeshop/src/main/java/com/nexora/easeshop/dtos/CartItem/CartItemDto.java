package com.nexora.easeshop.dtos.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Integer quantity;

    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImageUrl;

    public static CartItemDto fromEntity(com.nexora.easeshop.models.CartItem cartItem) {
        if (cartItem == null) return null;

        var product = cartItem.getProduct();

        return CartItemDto.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .productId(product != null ? product.getId() : null)
                .productName(product != null ? product.getName() : null)
                .productPrice(product != null ? product.getPrice() : null)
                .productImageUrl(product != null ? product.getImageUrl() : null)
                .build();
    }
}
