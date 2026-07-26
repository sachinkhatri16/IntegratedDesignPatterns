package com.foodordering.service;

import com.foodordering.model.*;
import com.foodordering.behavioral.OrderObserver;
import com.foodordering.behavioral.OrderStatusManager;
import com.foodordering.behavioral.NotificationObserverFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * NotificationService handles all notifications using Observer pattern
 */
public class NotificationService {
    private Map<String, OrderStatusManager> orderObservers;

    public NotificationService() {
        this.orderObservers = new HashMap<>();
    }

    public void setupNotifications(Order order, Customer customer) {
        OrderStatusManager manager = new OrderStatusManager(order);
        manager.attach(NotificationObserverFactory.createEmailObserver(customer.getEmail()));
        manager.attach(NotificationObserverFactory.createSMSObserver(customer.getPhone()));
        manager.attach(NotificationObserverFactory.createPushObserver("DEVICE_" + customer.getUserId()));
        orderObservers.put(order.getOrderId(), manager);
    }

    public void notifyOrderStatusChange(String orderId, OrderStatus newStatus) {
        OrderStatusManager manager = orderObservers.get(orderId);
        if (manager != null) {
            manager.updateStatus(newStatus);
        }
    }

    public void sendOrderConfirmation(Order order) {
        System.out.println("📬 Sending order confirmation notifications for Order #" + order.getOrderId());
        notifyOrderStatusChange(order.getOrderId(), OrderStatus.CONFIRMED);
    }

    public void sendDeliveryNotification(Order order) {
        System.out.println("🚗 Sending delivery status notifications for Order #" + order.getOrderId());
        notifyOrderStatusChange(order.getOrderId(), OrderStatus.IN_DELIVERY);
    }

    public void sendDeliveryCompleteNotification(Order order) {
        System.out.println("✅ Sending delivery complete notifications for Order #" + order.getOrderId());
        notifyOrderStatusChange(order.getOrderId(), OrderStatus.DELIVERED);
    }
}

