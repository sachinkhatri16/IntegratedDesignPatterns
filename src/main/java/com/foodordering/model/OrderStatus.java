package com.foodordering.model;

/**
 * Enum for order status - implements STATE PATTERN
 */
public enum OrderStatus {
    PENDING("Pending", "Order placed, awaiting restaurant confirmation"),
    CONFIRMED("Confirmed", "Restaurant confirmed the order"),
    PREPARING("Preparing", "Food is being prepared"),
    READY("Ready", "Food is ready for pickup/delivery"),
    PICKED_UP("Picked Up", "Order has been picked up by delivery person"),
    ON_THE_WAY("On the Way", "Order is on the way to customer"),
    IN_DELIVERY("In Delivery", "Order is being delivered"),
    DELIVERED("Delivered", "Order delivered to customer"),
    CANCELLED("Cancelled", "Order cancelled"),
    UPDATING("Updating", "Order is being updated"),
    FAILED("Failed", "Order payment or processing failed");

    private final String status;
    private final String description;

    OrderStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
