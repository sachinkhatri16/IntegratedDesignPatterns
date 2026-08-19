package com.foodordering.behavioral;

import com.foodordering.model.OrderStatus;

/**
 * STATE PATTERN - OrderState
 * Allows Order to change behavior based on its state
 */

// State
public interface OrderState {
    void confirm(OrderContext context);
    void prepare(OrderContext context);
    void ready(OrderContext context);
    void pickUp(OrderContext context);
    void setOnWay(OrderContext context);
    void deliver(OrderContext context);
    void cancel(OrderContext context);
    String getStateName();
}

abstract class AbstractOrderState implements OrderState {
    @Override public void confirm(OrderContext context) { System.out.println("  ✗ Cannot confirm"); }
    @Override public void prepare(OrderContext context) { System.out.println("  ✗ Cannot prepare"); }
    @Override public void ready(OrderContext context) { System.out.println("  ✗ Cannot mark ready"); }
    @Override public void pickUp(OrderContext context) { System.out.println("  ✗ Cannot pick up"); }
    @Override public void setOnWay(OrderContext context) { System.out.println("  ✗ Cannot set on way"); }
    @Override public void deliver(OrderContext context) { System.out.println("  ✗ Cannot deliver"); }
    @Override public void cancel(OrderContext context) { System.out.println("  ✗ Cannot cancel"); }
}

// Concrete States
class PendingOrderState extends AbstractOrderState {
    @Override
    public void confirm(OrderContext context) {
        System.out.println("  ✓ Order confirmed");
        context.setState(new ConfirmedOrderState());
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

class ConfirmedOrderState extends AbstractOrderState {
    @Override
    public void prepare(OrderContext context) {
        System.out.println("  ✓ Order preparing");
        context.setState(new PreparingOrderState());
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

class PreparingOrderState extends AbstractOrderState {
    @Override
    public void ready(OrderContext context) {
        System.out.println("  ✓ Order ready for delivery");
        context.setState(new ReadyOrderState());
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

class ReadyOrderState extends AbstractOrderState {
    @Override
    public void pickUp(OrderContext context) {
        System.out.println("  ✓ Order picked up by delivery person");
        context.setState(new PickedUpOrderState());
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

class PickedUpOrderState extends AbstractOrderState {
    @Override
    public void setOnWay(OrderContext context) {
        System.out.println("  ✓ Order is on the way");
        context.setState(new OnWayOrderState());
    }

    @Override
    public String getStateName() {
        return "PICKED_UP";
    }
}

class OnWayOrderState extends AbstractOrderState {
    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✓ Order delivered");
        context.setState(new DeliveredOrderState());
    }

    @Override
    public String getStateName() {
        return "ON_THE_WAY";
    }
}

class InDeliveryOrderState extends AbstractOrderState {
    @Override
    public void deliver(OrderContext context) {
        System.out.println("  ✓ Order delivered");
        context.setState(new DeliveredOrderState());
    }

    @Override
    public String getStateName() {
        return "IN_DELIVERY";
    }
}

class DeliveredOrderState extends AbstractOrderState {
    @Override
    public String getStateName() {
        return "DELIVERED";
    }
}

class CancelledOrderState extends AbstractOrderState {
    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}
