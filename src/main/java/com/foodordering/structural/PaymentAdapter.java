package com.foodordering.structural;

/**
 * ADAPTER PATTERN - PaymentAdapter
 * Adapts different payment gateways to a common interface
 */

// Adaptee classes
class KhaltiPaymentGateway {
    public boolean sendPayment(double amount, String mobileNumber) {
        System.out.println("  → Processing payment via Khalti: NPR " + amount);
        return amount > 0;
    }

    public boolean cancelPayment(String khaltiTransactionId) {
        System.out.println("  → Khalti payment cancelled: " + khaltiTransactionId);
        return true;
    }
}

class EsewaPaymentGateway {
    public boolean transferMoney(double amount, String phoneNumber) {
        System.out.println("  → Processing payment via eSewa: NPR " + amount);
        return amount > 0;
    }
}

class BankTransferGateway {
    public boolean transferToBank(double amount, String accountNumber) {
        System.out.println("  → Processing bank transfer: NPR " + amount);
        return amount > 0;
    }
}

// Adapter implementations
class KhaltiAdapter implements PaymentProcessor {
    private KhaltiPaymentGateway khalti;
    private String mobileNumber;

    public KhaltiAdapter(String mobileNumber) {
        this.khalti = new KhaltiPaymentGateway();
        this.mobileNumber = mobileNumber;
    }

    @Override
    public boolean processPayment(double amount, String accountId) {
        return khalti.sendPayment(amount, mobileNumber);
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        return khalti.cancelPayment(transactionId);
    }

    @Override
    public String getPaymentMethodName() {
        return "KHALTI";
    }
}

class EsewaAdapter implements PaymentProcessor {
    private EsewaPaymentGateway esewa;
    private String phoneNumber;

    public EsewaAdapter(String phoneNumber) {
        this.esewa = new EsewaPaymentGateway();
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean processPayment(double amount, String accountId) {
        return esewa.transferMoney(amount, phoneNumber);
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        System.out.println("  → eSewa refund processed: NPR " + amount);
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "ESEWA";
    }
}

class BankTransferAdapter implements PaymentProcessor {
    private BankTransferGateway bank;
    private String accountNumber;

    public BankTransferAdapter(String accountNumber) {
        this.bank = new BankTransferGateway();
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean processPayment(double amount, String accountId) {
        return bank.transferToBank(amount, accountNumber);
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        System.out.println("  → Bank refund initiated: NPR " + amount);
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "BANK_TRANSFER";
    }
}
