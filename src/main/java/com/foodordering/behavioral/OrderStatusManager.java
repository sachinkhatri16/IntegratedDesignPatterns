package com.foodordering.behavioral;

import java.util.ArrayList;
import java.util.List;
import com.foodordering.model.Order;
import com.foodordering.model.OrderStatus;

/**
 * Subject for Observer pattern
 */
public class OrderStatusManager {
    private List<OrderObserver> observers = new ArrayList<>();
    private Order order;

    public OrderStatusManager(Order order) {
        this.order = order;
    }

    public void attach(OrderObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("  ✓ Observer attached");
        }
    }

    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }

    public void updateStatus(OrderStatus newStatus) {
        order.setStatus(newStatus);
        notifyObservers();
    }

    private void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.update(order, order.getStatus());
        }
    }

    public Order getOrder() {
        return order;
    }
}
