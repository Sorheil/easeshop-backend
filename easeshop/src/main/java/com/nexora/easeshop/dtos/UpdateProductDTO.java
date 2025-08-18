package com.nexora.easeshop.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateProductDTO {

    @NotNull(message = "L'ID du produit est requis.")
    private Long id;

    private String name;

    private String description;

    private Double price;

    private String imageProductUrl;
}
