package com.nexora.easeshop.controllers;

import com.nexora.easeshop.dtos.CinetPayPaymentRequest;
import com.nexora.easeshop.services.OrderService;
import com.nexora.easeshop.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/cinetpay")
    public ResponseEntity<Map<String, String>> createCinetPayPayment() {
        String paymentUrl = orderService.createOrder();
        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }

    @PostMapping("/cinetpay/notify")
    public ResponseEntity<?> handleCinetPayNotification(@RequestBody Map<String, Object> payload) {
        log.info("Notification CinetPay reçue : {}", payload);
        return ResponseEntity.ok().build();
    }
}

