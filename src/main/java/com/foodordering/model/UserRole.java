package com.foodordering.model;

/**
 * Enum for different user roles in the system
 */
public enum UserRole {
    CUSTOMER("Customer"),
    RESTAURANT_OWNER("Restaurant Owner"),
    ADMIN("Administrator"),
    DELIVERY_AGENT("Delivery Agent");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
