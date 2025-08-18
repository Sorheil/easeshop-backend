package com.nexora.easeshop.security;

import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.models.RefreshToken;
import com.nexora.easeshop.repositories.CustomerRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${easeshop.jwt.secret}")
    private String jwtSecret;

    @Value("${easeshop.jwt.expirationtime}")
    private int jwtExpirationTime;

    @Value("${easeshop.jwt.refreshExpiration}")
    private int refreshExpirationTime;

    private final CustomerRepository customerRepository;

    public String generateToken(Authentication authentication) {
        Customer customer = (Customer) authentication.getPrincipal();
        return Jwts.
                builder().
                setSubject(customer.getEmail()).
                setIssuedAt(new java.util.Date()).
                setExpiration(new java.util.Date(System.currentTimeMillis() + jwtExpirationTime)).
                signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).
                compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().
                    setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).
                    build().
                    parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().
                setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).
                build().
                parseClaimsJws(token).
                getBody().
                getSubject();
    }

}
