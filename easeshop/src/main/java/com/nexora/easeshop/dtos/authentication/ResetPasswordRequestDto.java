package com.nexora.easeshop.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDto(
        @NotBlank(message = "Le token est obligatoire")
        String token,

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+])(?=.*[0-9]).{6,}$", message = "Le mot de passe doit contenir au moins une majuscule, un chiffre et un caractère spécial")
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 6, max = 100, message = "Le mot de passe doit contenir au moins 6 caractères")
        String password
) {
}
