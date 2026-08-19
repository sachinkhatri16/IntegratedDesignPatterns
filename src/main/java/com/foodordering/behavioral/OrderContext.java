package com.foodordering.behavioral;

/**
 * Context for OrderState pattern
 */
public class OrderContext {
    private OrderState currentState;

    public OrderContext() {
        this.currentState = new PendingOrderState();
    }

    public void setState(OrderState state) {
        this.currentState = state;
    }

    public OrderState getState() {
        return currentState;
    }

    public String getCurrentStatus() {
        return currentState.getStateName();
    }

    public void confirm() { currentState.confirm(this); }
    public void prepare() { currentState.prepare(this); }
    public void markReady() { currentState.ready(this); }
    public void pickUp() { currentState.pickUp(this); }
    public void setOnWay() { currentState.setOnWay(this); }
    public void deliver() { currentState.deliver(this); }
    public void cancel() { currentState.cancel(this); }
}
