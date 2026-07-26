package com.foodordering.behavioral;

import com.foodordering.model.Order;
import com.foodordering.model.OrderStatus;

/**
 * OBSERVER PATTERN - OrderObserver
 * Notifies observers about order status changes
 */

public interface OrderObserver {
    void update(Order order, OrderStatus newStatus);
}

// Concrete Observers
class EmailNotificationObserver implements OrderObserver {
    private String email;

    public EmailNotificationObserver(String email) {
        this.email = email;
    }

    @Override
    public void update(Order order, OrderStatus newStatus) {
        System.out.println("  📧 Email sent to " + email + " - Order " + order.getOrderId() + 
                          " is now " + newStatus.getStatus());
    }
}

class SMSNotificationObserver implements OrderObserver {
    private String phoneNumber;

    public SMSNotificationObserver(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void update(Order order, OrderStatus newStatus) {
        System.out.println("  📱 SMS sent to " + phoneNumber + " - Order " + order.getOrderId() + 
                          " status: " + newStatus.getStatus());
    }
}

class PushNotificationObserver implements OrderObserver {
    private String deviceId;

    public PushNotificationObserver(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void update(Order order, OrderStatus newStatus) {
        System.out.println("  🔔 Push notification sent to device " + deviceId + " - " + 
                          order.getOrderId() + ": " + newStatus.getDescription());
    }
}
