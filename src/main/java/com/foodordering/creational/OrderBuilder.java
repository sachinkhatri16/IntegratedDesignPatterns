package com.foodordering.creational;

import com.foodordering.model.Order;
import com.foodordering.model.OrderItem;
import com.foodordering.model.Customer;
import com.foodordering.model.RestaurantOwner;
import com.foodordering.model.MenuItem;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BUILDER PATTERN - OrderBuilder
 * Provides a flexible way to construct complex Order objects
 */
public class OrderBuilder {
    private String orderId;
    private Customer customer;
    private RestaurantOwner restaurant;
    private List<OrderItem> items;
    private double deliveryFee;
    private String deliveryAddress;
    private String paymentMethod;
    private String specialInstructions;

    public OrderBuilder(String orderId, Customer customer, RestaurantOwner restaurant) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.deliveryFee = 50.0;
        this.paymentMethod = "WALLET";
        this.specialInstructions = "";
    }

    public OrderBuilder addItem(MenuItem menuItem, int quantity) {
        items.add(new OrderItem(menuItem, quantity));
        return this;
    }

    public OrderBuilder setDeliveryFee(double fee) {
        this.deliveryFee = fee;
        return this;
    }

    public OrderBuilder setDeliveryAddress(String address) {
        this.deliveryAddress = address;
        return this;
    }

    public OrderBuilder setPaymentMethod(String method) {
        this.paymentMethod = method;
        return this;
    }

    public OrderBuilder setSpecialInstructions(String instructions) {
        this.specialInstructions = instructions;
        return this;
    }

    public Order build() {
        if (customer == null || restaurant == null) {
            throw new IllegalStateException("Customer and Restaurant are required");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }
        if (deliveryAddress == null || deliveryAddress.isEmpty()) {
            throw new IllegalStateException("Delivery address is required");
        }

        Order order = new Order(orderId, customer, restaurant);
        for (OrderItem item : items) {
            order.addItem(item);
        }
        order.setDeliveryFee(deliveryFee);
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(paymentMethod);
        order.setSpecialInstructions(specialInstructions);
        order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(45));

        return order;
    }
}
