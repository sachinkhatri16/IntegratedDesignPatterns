package com.foodordering.model;

/**
 * OrderItem representing a single item in an order
 */
public class OrderItem {
    private MenuItem menuItem;
    private int quantity;
    private double unitPrice;
    private double subtotal;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitPrice = menuItem.getPrice();
        this.subtotal = unitPrice * quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = unitPrice * quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return String.format("%-20s x %d @ NPR %.2f = NPR %.2f", 
                menuItem.getName(), quantity, unitPrice, subtotal);
    }
}
