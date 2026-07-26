package com.foodordering.structural;

/**
 * Factory for creating PaymentProcessor (Adapter) instances
 */
public class PaymentAdapterFactory {
    
    public static PaymentProcessor createKhaltiAdapter(String mobileNumber) {
        return new KhaltiAdapter(mobileNumber);
    }
    
    public static PaymentProcessor createEsewaAdapter(String phoneNumber) {
        return new EsewaAdapter(phoneNumber);
    }
    
    public static PaymentProcessor createBankTransferAdapter(String accountNumber) {
        return new BankTransferAdapter(accountNumber);
    }
}
