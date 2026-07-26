package com.foodordering.service;

import com.foodordering.model.*;

/**
 * SecurityService handles role-based access control
 */
public class SecurityService {

    public boolean authorizeCustomer(User user) {
        return user instanceof Customer && user.isActive();
    }

    public boolean authorizeRestaurant(User user) {
        return user instanceof RestaurantOwner && user.isActive();
    }

    public boolean authorizeAdmin(User user) {
        return user instanceof Administrator && user.isActive();
    }

    public boolean canPlaceOrder(User user) {
        return user instanceof Customer && user.isActive();
    }

    public boolean canManageMenu(User user) {
        return user instanceof RestaurantOwner && ((RestaurantOwner)user).isVerified();
    }

    public boolean canApproveRestaurants(User user) {
        return user instanceof Administrator && ((Administrator)user).canApproveRestaurants();
    }

    public boolean canManageUsers(User user) {
        return user instanceof Administrator && ((Administrator)user).canManageUsers();
    }

    public void logAccessAttempt(User user, String action, boolean authorized) {
        String status = authorized ? "✓ ALLOWED" : "✗ DENIED";
        System.out.println("[LOG] " + status + " - " + user.getName() + " attempted " + action);
    }
}
