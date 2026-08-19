package com.foodordering.behavioral;

/**
 * STRATEGY PATTERN - PaymentStrategy
 * Encapsulates different payment algorithms
 */

public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentMethodName();
}

class WalletPaymentStrategy implements PaymentStrategy {
    private double walletBalance;

    public WalletPaymentStrategy(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    @Override
    public boolean pay(double amount) {
        if (walletBalance >= amount) {
            walletBalance -= amount;
            System.out.println("  ✓ Paid NPR " + amount + " via Wallet");
            return true;
        }
        System.out.println("  ✗ Insufficient wallet balance");
        return false;
    }

    @Override
    public String getPaymentMethodName() {
        return "WALLET";
    }
}

class CreditCardPaymentStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cardholderName;

    public CreditCardPaymentStrategy(String cardNumber, String cardholderName) {
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("  ✓ Paid NPR " + amount + " via Credit Card (****" + 
                          cardNumber.substring(cardNumber.length() - 4) + ")");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "CREDIT_CARD";
    }
}

class CashOnDeliveryStrategy implements PaymentStrategy {
    private String recipientName;

    public CashOnDeliveryStrategy(String recipientName) {
        this.recipientName = recipientName;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("  ✓ Cash on Delivery: NPR " + amount + " to be paid to " + recipientName);
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "CASH_ON_DELIVERY";
    }
}

class DigitalWalletPaymentStrategy implements PaymentStrategy {
    private String walletProvider;
    private String accountId;

    public DigitalWalletPaymentStrategy(String walletProvider, String accountId) {
        this.walletProvider = walletProvider;
        this.accountId = accountId;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("  ✓ Paid NPR " + amount + " via " + walletProvider + " (Account: " + accountId + ")");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return walletProvider.toUpperCase();
    }
}

class KhaltiPaymentStrategy extends DigitalWalletPaymentStrategy {
    public KhaltiPaymentStrategy(String accountId) {
        super("KHALTI", accountId);
    }
}

class EsewaPaymentStrategy extends DigitalWalletPaymentStrategy {
    public EsewaPaymentStrategy(String accountId) {
        super("ESEWA", accountId);
    }
}
