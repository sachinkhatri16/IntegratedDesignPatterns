package com.foodordering.structural;

import com.foodordering.model.Order;

/**
 * Basic Order component for Decorator pattern
 */
public class BasicOrder implements OrderComponent {
    private Order order;

    public BasicOrder(Order order) {
        this.order = order;
    }

    @Override
    public String getDescription() {
        return "Basic Order: " + order.getOrderId();
    }

    @Override
    public double getTotalPrice() {
        return order.getTotalAmount() + order.getDeliveryFee();
    }

    public Order getOrder() {
        return order;
    }
}
