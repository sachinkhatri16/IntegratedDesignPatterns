package com.foodordering.behavioral;

import com.foodordering.model.Order;
import java.util.List;
import java.util.ArrayList;

/**
 * COMMAND PATTERN - OrderCommand
 * Encapsulates requests as objects for queuing, logging, and undo/redo
 */

public interface OrderCommand {
    void execute();
    void undo();
    String getDescription();
}

class PlaceOrderCommand implements OrderCommand {
    private Order order;
    private OrderRepository repository;

    public PlaceOrderCommand(Order order, OrderRepository repository) {
        this.order = order;
        this.repository = repository;
    }

    @Override
    public void execute() {
        System.out.println("  ➜ Executing: Place Order #" + order.getOrderId());
        repository.save(order);
        order.setStatus(com.foodordering.model.OrderStatus.CONFIRMED);
    }

    @Override
    public void undo() {
        System.out.println("  ↶ Undoing: Remove Order #" + order.getOrderId());
        repository.delete(order.getOrderId());
    }

    @Override
    public String getDescription() {
        return "Place Order " + order.getOrderId();
    }
}

class CancelOrderCommand implements OrderCommand {
    private Order order;
    private OrderRepository repository;
    private com.foodordering.model.OrderStatus previousStatus;

    public CancelOrderCommand(Order order, OrderRepository repository) {
        this.order = order;
        this.repository = repository;
        this.previousStatus = order.getStatus();
    }

    @Override
    public void execute() {
        System.out.println("  ➜ Executing: Cancel Order #" + order.getOrderId());
        order.setStatus(com.foodordering.model.OrderStatus.CANCELLED);
        repository.update(order);
    }

    @Override
    public void undo() {
        System.out.println("  ↶ Undoing: Restore Order #" + order.getOrderId() + " to " + previousStatus.getStatus());
        order.setStatus(previousStatus);
        repository.update(order);
    }

    @Override
    public String getDescription() {
        return "Cancel Order " + order.getOrderId();
    }
}

class DeliverOrderCommand implements OrderCommand {
    private Order order;
    private OrderRepository repository;

    public DeliverOrderCommand(Order order, OrderRepository repository) {
        this.order = order;
        this.repository = repository;
    }

    @Override
    public void execute() {
        System.out.println("  ➜ Executing: Deliver Order #" + order.getOrderId());
        order.setStatus(com.foodordering.model.OrderStatus.DELIVERED);
        repository.update(order);
    }

    @Override
    public void undo() {
        System.out.println("  ↶ Undoing: Set Order #" + order.getOrderId() + " back to On the Way");
        order.setStatus(com.foodordering.model.OrderStatus.ON_THE_WAY);
        repository.update(order);
    }

    @Override
    public String getDescription() {
        return "Deliver Order " + order.getOrderId();
    }
}

class UpdateOrderCommand implements OrderCommand {
    private Order order;
    private OrderRepository repository;
    private List<com.foodordering.model.OrderItem> newItems;
    private List<com.foodordering.model.OrderItem> oldItems;

    public UpdateOrderCommand(Order order, List<com.foodordering.model.OrderItem> newItems, OrderRepository repository) {
        this.order = order;
        this.newItems = newItems;
        this.repository = repository;
        this.oldItems = new java.util.ArrayList<>(order.getItems());
    }

    @Override
    public void execute() {
        System.out.println("  ➜ Executing: Update Order #" + order.getOrderId());
        order.setItems(newItems);
        order.setStatus(com.foodordering.model.OrderStatus.UPDATING);
        repository.update(order);
        order.setStatus(com.foodordering.model.OrderStatus.CONFIRMED);
    }

    @Override
    public void undo() {
        System.out.println("  ↶ Undoing: Restore original items for Order #" + order.getOrderId());
        order.setItems(oldItems);
        repository.update(order);
    }

    @Override
    public String getDescription() {
        return "Update Order Items for " + order.getOrderId();
    }
}
