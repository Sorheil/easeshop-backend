package com.nexora.easeshop.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class ResetPasswordTokenService {
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateRawToken() {
        // UUID + random suffix = uniqueness and unpredactibility of token
        String uuid = UUID.randomUUID().toString();
        byte[] rand = new byte[24];
        secureRandom.nextBytes(rand);
        return uuid + "-" + Base64.getUrlEncoder().withoutPadding().encodeToString(rand);
    }

    public String hashToken(String rawToken) {
        // to hide the token in case of database theft
        // SHA-256 hex
        return DigestUtils.sha256Hex(rawToken);
    }
}
