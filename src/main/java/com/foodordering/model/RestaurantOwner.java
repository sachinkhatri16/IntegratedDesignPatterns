package com.foodordering.model;

import java.util.ArrayList;
import java.util.List;

/**
 * RestaurantOwner class representing a restaurant in the system
 */
public class RestaurantOwner extends User {
    private String restaurantName;
    private String restaurantAddress;
    private List<MenuItem> menu;
    private boolean isVerified;
    private double totalRevenue;

    public RestaurantOwner(String userId, String name, String email, String phone, String restaurantName) {
        super(userId, name, email, phone, UserRole.RESTAURANT_OWNER);
        this.restaurantName = restaurantName;
        this.restaurantAddress = "";
        this.menu = new ArrayList<>();
        this.isVerified = false;
        this.totalRevenue = 0.0;
    }

    @Override
    public void displayRole() {
        System.out.println("I am a Restaurant Owner");
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String address) {
        this.restaurantAddress = address;
    }

    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu);
    }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    public void removeMenuItem(String itemId) {
        menu.removeIf(item -> item.getItemId().equals(itemId));
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void verify() {
        this.isVerified = true;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void addRevenue(double amount) {
        this.totalRevenue += amount;
    }
}
