package com.foodordering.structural;

import com.foodordering.model.User;

/**
 * PROXY PATTERN - UserProxy
 * Controls access to User objects and performs lazy initialization
 */
public class UserProxy {
    private User realUser;
    private String userId;
    private String userType;
    private boolean isAuthorized;

    public UserProxy(String userId, String userType) {
        this.userId = userId;
        this.userType = userType;
        this.isAuthorized = false;
        this.realUser = null;
    }

    public void authorize() {
        this.isAuthorized = true;
    }

    public User getUser() {
        if (!isAuthorized) {
            throw new SecurityException("User not authorized to access: " + userId);
        }
        if (realUser == null) {
            loadUser();
        }
        return realUser;
    }

    private void loadUser() {
        System.out.println("  [Proxy] Lazily loading user from database: " + userId);
        realUser = createUserInstance();
    }

    private User createUserInstance() {
        switch (userType) {
            case "CUSTOMER":
                return new com.foodordering.model.Customer(userId, "User " + userId, "user@email.com", "98xxxxxxxx");
            case "RESTAURANT":
                return new com.foodordering.model.RestaurantOwner(userId, "Owner " + userId, "owner@email.com", "98xxxxxxxx", "Restaurant");
            case "ADMIN":
                return new com.foodordering.model.Administrator(userId, "Admin " + userId, "admin@email.com", "98xxxxxxxx", "STANDARD");
            default:
                return null;
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }
}
