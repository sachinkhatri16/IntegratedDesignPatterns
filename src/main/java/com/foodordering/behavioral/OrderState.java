package com.foodordering.behavioral;

import com.foodordering.model.OrderStatus;

/**
 * STATE PATTERN - OrderState
 * Allows Order to change behavior based on its state
 */

// State interface
public interface OrderState {
    void confirm(OrderContext context);
    void prepare(OrderContext context);
    void ready(OrderContext context);
    void deliver(OrderContext context);
    void cancel(OrderContext context);
    String getStateName();
}

// Concrete States
class PendingOrderState implements OrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✓ Order confirmed");
        context.setState(new ConfirmedOrderState());
    }

    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✗ Cannot prepare before confirming");
    }

    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✗ Cannot mark ready");
    }

    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✗ Cannot deliver");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("  ✓ Order cancelled");
        context.setState(new CancelledOrderState());
    }

    @Override
    public String getStateName() {
        return "PENDING";
    }
}

class ConfirmedOrderState implements OrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✗ Already confirmed");
    }

    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✓ Order preparing");
        context.setState(new PreparingOrderState());
    }

    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✗ Not yet preparing");
    }

    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✗ Not ready for delivery");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("  ✓ Order cancelled");
        context.setState(new CancelledOrderState());
    }

    @Override
    public String getStateName() {
        return "CONFIRMED";
    }
}

class PreparingOrderState implements OrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✗ Already confirmed");
    }

    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✗ Already preparing");
    }

    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✓ Order ready for delivery");
        context.setState(new ReadyOrderState());
    }

    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✗ Not ready yet");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("  ~ Order will be cancelled after preparation");
        context.setState(new CancelledOrderState());
    }

    @Override
    public String getStateName() {
        return "PREPARING";
    }
}

class ReadyOrderState implements OrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✗ Already confirmed");
    }

    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✗ Already prepared");
    }

    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✗ Already ready");
    }

    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✓ Order in delivery");
        context.setState(new InDeliveryOrderState());
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("  ~ Refund initiated, order cancelled");
        context.setState(new CancelledOrderState());
    }

    @Override
    public String getStateName() {
        return "READY";
    }
}

class InDeliveryOrderState implements OrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✗ Already confirmed");
    }

    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✗ Already prepared");
    }

    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✗ Already ready");
    }

    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✓ Order delivered");
        context.setState(new DeliveredOrderState());
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("  ✗ Cannot cancel during delivery");
    }

    @Override
    public String getStateName() {
        return "IN_DELIVERY";
    }
}

class DeliveredOrderState implements OrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✗ Already delivered");
    }

    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✗ Already delivered");
    }

    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✗ Already delivered");
    }

    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✗ Already delivered");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("  ✗ Cannot cancel delivered order");
    }

    @Override
    public String getStateName() {
        return "DELIVERED";
    }
}

class CancelledOrderState implements OrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✗ Order is cancelled");
    }

    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✗ Order is cancelled");
    }

    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✗ Order is cancelled");
    }

    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✗ Order is cancelled");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("  ✗ Already cancelled");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}
