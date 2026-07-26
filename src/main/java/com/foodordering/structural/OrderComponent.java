package com.foodordering.structural;

/**
 * Component interface for Decorator pattern
 */
public interface OrderComponent {
    String getDescription();
    double getTotalPrice();
}
