package com.foodordering.structural;

import com.foodordering.model.Order;

/**
 * DECORATOR PATTERN - OrderDecorator
 * Adds additional responsibilities to Order objects dynamically
 */

// Decorators
abstract class OrderDecorator implements OrderComponent {
    protected OrderComponent order;

    public OrderDecorator(OrderComponent order) {
        this.order = order;
    }
}

class GiftWrapDecorator extends OrderDecorator {
    public GiftWrapDecorator(OrderComponent order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Gift Wrap";
    }

    @Override
    public double getTotalPrice() {
        return order.getTotalPrice() + 50.0;
    }
}

class PriorityDeliveryDecorator extends OrderDecorator {
    public PriorityDeliveryDecorator(OrderComponent order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Priority Delivery";
    }

    @Override
    public double getTotalPrice() {
        return order.getTotalPrice() + 100.0;
    }
}

class InsuranceDecorator extends OrderDecorator {
    public InsuranceDecorator(OrderComponent order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Order Insurance";
    }

    @Override
    public double getTotalPrice() {
        return order.getTotalPrice() + 25.0;
    }
}

class LoyaltyDecorator extends OrderDecorator {
    private double discountPercentage;

    public LoyaltyDecorator(OrderComponent order, double discountPercentage) {
        super(order);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Loyalty Discount (" + discountPercentage + "%)";
    }

    @Override
    public double getTotalPrice() {
        double basePrice = order.getTotalPrice();
        return basePrice - (basePrice * discountPercentage / 100);
    }
}
