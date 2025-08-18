package com.nexora.easeshop.services;

import com.nexora.easeshop.dtos.CinetPayPaymentRequest;
import com.nexora.easeshop.exceptions.CinetPayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient cinetPayClient;

    @Value("${cinetpay.api-key}")
    private String cinetPayApiKey;

    @Value("${cinetpay.site-id}")
    private String cinetPaySiteId;

    @Value("${cinetpay.currency}")
    private String cinetPayCurrency;

    @Value("${cinetpay.notify-url}")
    private String cinetPayNotifyUrl;

    @Value("${cinetpay.return-url}")
    private String cinetPayReturnUrl;

    @Value("${cinetpay.channels}")
    private String cinetPayChannels;

    @Value("${cinetpay.lang}")
    private String cinetPayLang;

    public String generateCinetPayPaymentUrl(String transactionId, BigDecimal amount) {

        CinetPayPaymentRequest request = new CinetPayPaymentRequest();
        request.apikey = cinetPayApiKey;
        request.site_id = cinetPaySiteId;
        request.transaction_id = transactionId;
        request.amount = amount
                .divide(new BigDecimal("5"), 0, RoundingMode.UP)
                .multiply(new BigDecimal("5"))
                .intValueExact();
        request.currency = cinetPayCurrency;
        request.description = "Paiement pour la commande " + transactionId;
        request.notify_url = cinetPayNotifyUrl;
        request.return_url = cinetPayReturnUrl;
        request.channels = cinetPayChannels;
        request.lang = cinetPayLang;

        Map<String, Object> response = cinetPayClient.post()
                .uri("/payment")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("CinetPay API error: {}", clientResponse.statusCode());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("CinetPay API error body: {}", errorBody);
                                return Mono.error(new CinetPayException("Erreur lors de la communication avec CinetPay"));
                            });
                })
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        return (String) data.get("payment_url");
    }
}
