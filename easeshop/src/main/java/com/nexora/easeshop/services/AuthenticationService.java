package com.nexora.easeshop.services;

import com.nexora.easeshop.exceptions.EmailAlreadyUsedException;
import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.repositories.CustomerRepository;
import com.nexora.easeshop.repositories.RefreshTokenRepository;
import com.nexora.easeshop.security.UserDetailService;
import com.nexora.easeshop.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    public final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailService customerDetailService;
    private final AuthenticationManager authenticationManager;

    public HashMap<String, String> registerCustomer(String username, String password, String email) {
        //1-verifier que l'email n'existe pas deja
        if (!customerRepository.existsByEmail(email)) {
            //2-hasher le mot de passe
            String customerPasswordEncode = bCryptPasswordEncoder.encode(password);
            //3-enregistrer l'utilisateur
            Customer customerToSaveInDatabse = new Customer();
            customerToSaveInDatabse.setUsername(username);
            customerToSaveInDatabse.setEmail(email);
            customerToSaveInDatabse.setPassword(customerPasswordEncode);
            customerRepository.save(customerToSaveInDatabse);
            //4-authenticate the user
            return this.authenticateCustomer(email, password);
        }
        throw new EmailAlreadyUsedException(" l'adresse e-mail " + email + " est deja associée a un compte. Veuillez vous connecter ou utiliser une autre adresse email");
    }

    public Customer getCurrentUser() {
        Customer customerDetails = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customerDetails;
    }

    public HashMap<String, String> getDetailsUserAuthenticated() {
        Customer customerDetails = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HashMap<String, String> customerDetailsResponse = new HashMap<String, String>();
        customerDetailsResponse.put("username", customerDetails.getUsername());
        customerDetailsResponse.put("email", customerDetails.getEmail());
        return customerDetailsResponse;
    }

    public HashMap<String, String> authenticateCustomer(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateToken(authentication);
        String refreshToken = refreshTokenService.generateRefreshToken(email);
        HashMap<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        return response;
    }


}
