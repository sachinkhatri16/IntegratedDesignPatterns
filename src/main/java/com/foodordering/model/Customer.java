package com.foodordering.model;

/**
 * Customer class representing a food ordering customer
 */
public class Customer extends User {
    private double walletBalance;
    private String address;
    private int totalOrders;

    public Customer(String userId, String name, String email, String phone) {
        super(userId, name, email, phone, UserRole.CUSTOMER);
        this.walletBalance = 0.0;
        this.address = "";
        this.totalOrders = 0;
    }

    @Override
    public void displayRole() {
        System.out.println("I am a Customer");
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void addToWallet(double amount) {
        this.walletBalance += amount;
    }

    public boolean deductFromWallet(double amount) {
        if (walletBalance >= amount) {
            walletBalance -= amount;
            return true;
        }
        return false;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void incrementOrderCount() {
        this.totalOrders++;
    }
}
