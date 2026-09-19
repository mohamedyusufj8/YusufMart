package com.yusuf.yusufmart.service.payment;

import java.math.BigDecimal;

/**
 * Strategy interface for swappable payment processing channels.
 * Fulfills Section 12 requirement: Strategy pattern.
 */
public interface PaymentStrategy {
    PaymentResult process(BigDecimal amount, String accountIdentifier);

    class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String message;

        public PaymentResult(boolean success, String transactionId, String message) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getMessage() {
            return message;
        }
    }
}
