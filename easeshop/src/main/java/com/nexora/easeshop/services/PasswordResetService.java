package com.nexora.easeshop.services;

import com.nexora.easeshop.exceptions.InvalidTokenException;
import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.models.PasswordResetToken;
import com.nexora.easeshop.repositories.CustomerRepository;
import com.nexora.easeshop.repositories.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final CustomerRepository customerRepository;
    private final PasswordResetTokenRepository tokenRepo;
    private final ResetPasswordTokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;
    private final long PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES = 60;
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);


    @Transactional
    public void initiatePasswordReset(String email) {

        customerRepository.findByEmailIgnoreCase(email).ifPresent(customer -> {
            // remove previous tokens for this user (optional)
            tokenRepo.deleteAllByCustomer(customer);

            String rawToken = tokenService.generateRawToken();
            String hash = tokenService.hashToken(rawToken);

            PasswordResetToken prt = new PasswordResetToken();
            prt.setTokenHash(hash);
            prt.setCustomer(customer);
            prt.setExpiryDate(Instant.now().plus(PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES));
            prt.setUsed(false);
            tokenRepo.save(prt);

            String resetLink = frontendBaseUrl + "auth/reset-password?token=" + rawToken;
            try {
                emailService.sendPasswordResetEmail(customer.getEmail(), resetLink);
            } catch (Exception e) {
                logger.error("Erreur lors de l'envoi de l'email de réinitialisation de mot de passe à {}", customer.getEmail(), e);
            }
        });
        // Si utilisateur absent => silencieux (réponse générique)
    }

    @Transactional
    public void consumeResetTokenAndUpdatePassword(String rawToken, String newPassword) {
        String hash = tokenService.hashToken(rawToken);
        PasswordResetToken prt = tokenRepo.findByTokenHash(hash)
                .orElseThrow(() -> new InvalidTokenException("Token invalide"));

        if (prt.isUsed() || prt.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidTokenException("Token expiré ou déjà utilisé");
        }

        Customer customer = prt.getCustomer();
        if (customer == null) {
            throw new InvalidTokenException("Utilisateur introuvable");
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);

        // mark used
        prt.setUsed(true);
        tokenRepo.save(prt);
    }

}
