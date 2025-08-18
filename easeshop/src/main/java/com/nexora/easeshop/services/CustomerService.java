package com.nexora.easeshop.services;


import com.nexora.easeshop.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;


}
