package com.nexora.easeshop.services;

import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.models.RefreshToken;
import com.nexora.easeshop.repositories.CustomerRepository;
import com.nexora.easeshop.repositories.RefreshTokenRepository;
import com.nexora.easeshop.security.JwtUtils;
import com.nexora.easeshop.security.UserDetailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final CustomerRepository customerRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final UserDetailService userDetailService;

    // Durée de validité du token : 7 jours (par exemple)
    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    /**
     * Génère un refresh token associé à l'utilisateur courant
     */
    public String generateRefreshToken(String email) {

        Customer customer = customerRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email " + email + " non trouvé."));

        String tokenString = UUID.randomUUID().toString();

        RefreshToken token = new RefreshToken();
        token.setToken(tokenString);
        token.setCustomer(customer);
        token.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS));

        refreshTokenRepository.save(token);
        return tokenString;
    }

    /**
     * Retourne un nouveau JWT basé sur un refresh token valide
     */
    public Optional<String> generateNewAccessToken(String oldrefreshToken) {
        return refreshTokenRepository.findByToken(oldrefreshToken)
                .filter(token -> !isTokenExpired(token))
                .map(token -> {
                    Customer customer = token.getCustomer();
                    UserDetails userDetails = userDetailService.loadUserByUsername(customer.getEmail());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    return jwtUtils.generateToken(authentication);
                });
    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    /**
     * Vérifie si le token est expiré
     */
    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
