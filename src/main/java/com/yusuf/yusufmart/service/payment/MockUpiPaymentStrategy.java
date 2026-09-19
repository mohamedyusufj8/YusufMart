package com.yusuf.yusufmart.service.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock UPI Payment implementation of PaymentStrategy.
 */
public class MockUpiPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult process(BigDecimal amount, String upiId) {
        String txId = "UPI-TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, txId, "Mock UPI payment of $" + amount + " authorized successfully.");
    }
}
