package com.nexora.easeshop.repositories;

import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);
    void deleteAllByCustomer(Customer customer);
}