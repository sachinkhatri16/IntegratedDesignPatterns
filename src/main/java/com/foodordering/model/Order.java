package com.foodordering.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order class representing a customer's food order
 */
public class Order {
    private String orderId;
    private Customer customer;
    private RestaurantOwner restaurant;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalAmount;
    private double deliveryFee;
    private LocalDateTime orderTime;
    private LocalDateTime estimatedDeliveryTime;
    private String deliveryAddress;
    private String paymentMethod;
    private String specialInstructions;

    public Order(String orderId, Customer customer, RestaurantOwner restaurant) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.totalAmount = 0.0;
        this.deliveryFee = 0.0;
        this.orderTime = LocalDateTime.now();
        this.paymentMethod = "WALLET";
        this.specialInstructions = "";
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public RestaurantOwner getRestaurant() {
        return restaurant;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(OrderItem item) {
        items.add(item);
        totalAmount += item.getSubtotal();
    }

    public void setItems(List<OrderItem> items) {
        this.items = new ArrayList<>(items);
        this.totalAmount = items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }

    public void removeItem(String itemId) {
        items.removeIf(item -> {
            if (item.getMenuItem().getItemId().equals(itemId)) {
                totalAmount -= item.getSubtotal();
                return true;
            }
            return false;
        });
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getFinalAmount() {
        return totalAmount + deliveryFee;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime time) {
        this.estimatedDeliveryTime = time;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String address) {
        this.deliveryAddress = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String method) {
        this.paymentMethod = method;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String instructions) {
        this.specialInstructions = instructions;
    }

    @Override
    public String toString() {
        return String.format("Order #%s | Status: %s | Amount: NPR %.2f", orderId, status.getStatus(), getFinalAmount());
    }
}
