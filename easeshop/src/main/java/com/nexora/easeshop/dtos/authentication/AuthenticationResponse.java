package com.nexora.easeshop.dtos.authentication;

public record AuthenticationResponse(String message, String accessToken, String refreshToken) {
}
