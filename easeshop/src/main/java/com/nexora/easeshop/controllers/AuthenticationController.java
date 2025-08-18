package com.nexora.easeshop.controllers;

import com.nexora.easeshop.dtos.authentication.*;
import com.nexora.easeshop.exceptions.InvalidTokenException;
import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.security.JwtUtils;
import com.nexora.easeshop.services.AuthenticationService;
import com.nexora.easeshop.services.PasswordResetService;
import com.nexora.easeshop.services.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@Valid @RequestBody RegisterCustomerRequestDto registerCustomerRequestDto) {
        Map<String, String> response = new HashMap<>();
        HashMap<String, String> tokens = authenticationService.registerCustomer(registerCustomerRequestDto.username(), registerCustomerRequestDto.password(), registerCustomerRequestDto.email());
        return ResponseEntity.ok(new AuthenticationResponse("Connexion réussie !", tokens.get("accessToken"), tokens.get("refreshToken")));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody LoginCustomerRequestDto loginCustomerRequestDto) {
        HashMap<String, String> tokens = authenticationService.authenticateCustomer(loginCustomerRequestDto.email(), loginCustomerRequestDto.password());
        return ResponseEntity.ok(new AuthenticationResponse("Connexion réussie !", tokens.get("accessToken"), tokens.get("refreshToken")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authenticationService.getDetailsUserAuthenticated());
    }

    @GetMapping("/refreshaccesstoken")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        String oldRefreshToken = payload.get("refreshToken");
        Optional<String> jwt = refreshTokenService.generateNewAccessToken(oldRefreshToken);

        if (jwt.isPresent()) {
            return ResponseEntity.ok(Map.of("accesstoken", jwt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "refresh token is invalid or expired"));
        }
    }

    @PostMapping("/password-request")
    public ResponseEntity<?> passwordRequest(@Valid @RequestBody ForgotPasswordRequestDto req) {
        passwordResetService.initiatePasswordReset(req.email());
        return ResponseEntity.ok(Map.of("message", "Si cet email est enregistré, un lien de réinitialisation a été envoyé."));
    }

    @PostMapping("/password-change")
    public ResponseEntity<?> passwordChange(@Valid @RequestBody ResetPasswordRequestDto req) {
        passwordResetService.consumeResetTokenAndUpdatePassword(req.token(), req.password());
        return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour"));
    }
}
