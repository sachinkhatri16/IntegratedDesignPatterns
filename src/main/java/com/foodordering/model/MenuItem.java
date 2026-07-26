package com.foodordering.model;

/**
 * MenuItem representing a food item in a restaurant menu
 */
public class MenuItem {
    private String itemId;
    private String name;
    private String description;
    private double price;
    private boolean available;
    private String category;

    public MenuItem(String itemId, String name, String description, double price, String category) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = true;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%-5s | %-20s | NPR %.2f | %s", itemId, name, price, category);
    }
}
