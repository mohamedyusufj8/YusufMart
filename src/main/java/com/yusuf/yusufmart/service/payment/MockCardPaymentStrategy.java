package com.yusuf.yusufmart.service.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock Card Payment implementation of PaymentStrategy.
 */
public class MockCardPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult process(BigDecimal amount, String cardNumber) {
        String txId = "CARD-TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, txId, "Mock Card payment of $" + amount + " authorized successfully.");
    }
}
