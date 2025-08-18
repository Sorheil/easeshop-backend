package com.nexora.easeshop.dtos.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDto(
        @NotBlank(message = "L'e-mail est obligatoire") @Email(message = "L'e-mail doit être valide") String email) {
}
