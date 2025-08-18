package com.nexora.easeshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient cinetPayClient() {
        return WebClient.builder()
                .baseUrl("https://api-checkout.cinetpay.com/v2")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
