package com.nexora.easeshop.security;

import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return customer;
    }
}
