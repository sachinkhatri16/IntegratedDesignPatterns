package com.foodordering.structural;

/**
 * Payment Processor interface for Adapter pattern
 */
public interface PaymentProcessor {
    boolean processPayment(double amount, String accountId);
    boolean refundPayment(double amount, String transactionId);
    String getPaymentMethodName();
}
