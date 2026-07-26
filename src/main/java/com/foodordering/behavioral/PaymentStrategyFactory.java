package com.foodordering.behavioral;

/**
 * Factory for creating PaymentStrategy instances
 */
public class PaymentStrategyFactory {
    
    public static PaymentStrategy createWalletStrategy(double balance) {
        return new WalletPaymentStrategy(balance);
    }
    
    public static PaymentStrategy createCreditCardStrategy(String cardNumber, String cardholderName) {
        return new CreditCardPaymentStrategy(cardNumber, cardholderName);
    }
    
    public static PaymentStrategy createCashOnDeliveryStrategy(String recipientName) {
        return new CashOnDeliveryStrategy(recipientName);
    }
    
    public static PaymentStrategy createDigitalWalletStrategy(String provider, String accountId) {
        return new DigitalWalletPaymentStrategy(provider, accountId);
    }
}
