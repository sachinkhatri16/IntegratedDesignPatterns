package com.foodordering.service;

import com.foodordering.model.*;
import com.foodordering.behavioral.*;
import com.foodordering.creational.OrderBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * OrderService manages order lifecycle
 */
public class OrderService {
    private Map<String, Order> orderRepository;
    private OrderCommandInvoker commandInvoker;
    private static int orderCounter = 1;

    public OrderService() {
        this.orderRepository = new HashMap<>();
        this.commandInvoker = new OrderCommandInvoker();
    }

    public Order createOrder(Customer customer, RestaurantOwner restaurant, OrderBuilder builder) {
        Order order = builder.build();
        return order;
    }

    public void placeOrder(Order order) {
        MockOrderRepository repo = new MockOrderRepository();
        OrderCommand command = OrderCommandFactory.createPlaceOrderCommand(order, repo);
        commandInvoker.executeCommand(command);
        orderRepository.put(order.getOrderId(), order);
    }

    public void cancelOrder(Order order) {
        MockOrderRepository repo = new MockOrderRepository();
        OrderCommand command = OrderCommandFactory.createCancelOrderCommand(order, repo);
        commandInvoker.executeCommand(command);
    }

    public void updateOrderItems(Order order, List<OrderItem> newItems) {
        MockOrderRepository repo = new MockOrderRepository();
        OrderCommand command = OrderCommandFactory.createUpdateOrderCommand(order, newItems, repo);
        commandInvoker.executeCommand(command);
    }

    public void updateOrderStatus(Order order, OrderStatus newStatus) {
        order.setStatus(newStatus);
        orderRepository.put(order.getOrderId(), order);
    }

    public Order getOrder(String orderId) {
        return orderRepository.get(orderId);
    }

    public List<Order> getCustomerOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();
        for (Order order : orderRepository.values()) {
            if (order.getCustomer().equals(customer)) {
                orders.add(order);
            }
        }
        return orders;
    }

    // Mock implementation
    private static class MockOrderRepository implements OrderRepository {
        @Override
        public void save(Order order) {}
        @Override
        public void update(Order order) {}
        @Override
        public void delete(String orderId) {}
        @Override
        public Order findById(String orderId) { return null; }
    }
}
