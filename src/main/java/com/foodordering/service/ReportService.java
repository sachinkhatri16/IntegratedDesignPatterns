package com.foodordering.service;

import com.foodordering.model.*;

/**
 * ReportService generates various reports
 */
public class ReportService {
    
    public void generateSalesReport(RestaurantOwner restaurant) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SALES REPORT - " + restaurant.getRestaurantName());
        System.out.println("=".repeat(60));
        System.out.println("Owner: " + restaurant.getName());
        System.out.println("Total Revenue: NPR " + String.format("%.2f", restaurant.getTotalRevenue()));
        System.out.println("Status: " + (restaurant.isVerified() ? "Verified" : "Pending"));
        System.out.println("=".repeat(60) + "\n");
    }

    public void generateCustomerReport(Customer customer) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CUSTOMER REPORT - " + customer.getName());
        System.out.println("=".repeat(60));
        System.out.println("Customer ID: " + customer.getUserId());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Wallet Balance: NPR " + String.format("%.2f", customer.getWalletBalance()));
        System.out.println("Total Orders: " + customer.getTotalOrders());
        System.out.println("Address: " + customer.getAddress());
        System.out.println("=".repeat(60) + "\n");
    }

    public void generateOrderReport(Order order) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ORDER REPORT - #" + order.getOrderId());
        System.out.println("=".repeat(60));
        System.out.println("Customer: " + order.getCustomer().getName());
        System.out.println("Restaurant: " + order.getRestaurant().getRestaurantName());
        System.out.println("Status: " + order.getStatus().getStatus());
        System.out.println("\nItems:");
        for (OrderItem item : order.getItems()) {
            System.out.println("  - " + item);
        }
        System.out.println("\nSubtotal: NPR " + String.format("%.2f", order.getTotalAmount()));
        System.out.println("Delivery Fee: NPR " + String.format("%.2f", order.getDeliveryFee()));
        System.out.println("Total: NPR " + String.format("%.2f", order.getFinalAmount()));
        System.out.println("=".repeat(60) + "\n");
    }
}
