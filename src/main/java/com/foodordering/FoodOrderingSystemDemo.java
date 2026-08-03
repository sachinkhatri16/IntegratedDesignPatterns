package com.foodordering;

import com.foodordering.model.*;
import com.foodordering.creational.*;
import com.foodordering.structural.*;
import java.util.*;

public class FoodOrderingSystemDemo {
    private static Scanner scanner = new Scanner(System.in);
    private static FoodOrderingFacade facade = new FoodOrderingFacade();
    private static User currentUser = null;

    public static void main(String[] args) {
        // Print DB settings (mask password) so it's easy to verify env vars at startup
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/food_ordering");
        String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "");
        System.out.println("DB URL: " + dbUrl);
        System.out.println("DB USER: " + dbUser);
        System.out.println("DB PASSWORD: " + (dbPassword == null || dbPassword.isBlank() ? "(empty)" : "(provided, masked)"));

        DatabaseManager.getInstance().connect();
        
        printBanner("WELCOME TO ONLINE FOOD ORDERING SYSTEM");

        boolean running = true;
        while (running) {
            try {
                if (currentUser == null) {
                    running = showMainMenu();
                } else {
                    showUserMenu();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean showMainMenu() {
        System.out.println("1. Login");
        System.out.println("2. Register as Customer");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1: login(); break;
            case 2: register(); break;
            case 3: 
                DatabaseManager.getInstance().disconnect();
                System.out.println("Goodbye!");
                return false;
            default: System.out.println("Invalid choice!");
        }
        return true;
    }

    private static void showUserMenu() {
        System.out.println("\n--- User Menu ---");
        System.out.println("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        System.out.println("1. View Menu & Place Order");
        System.out.println("2. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1: placeOrder(); break;
            case 2: currentUser = null; break;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (facade.loginUser(userId, password)) {
            currentUser = facade.getUser(userId);
            System.out.println("✓ Login successful!");
        } else {
            System.out.println("✗ Invalid User ID or Password!");
        }
    }

    private static void register() {
        System.out.println("\n--- Registration ---");
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (facade.registerCustomer(userId, name, email, phone, password)) {
            System.out.println("✓ Registration successful! You can now login.");
        } else {
            System.out.println("✗ Registration failed! User ID might already exist.");
        }
    }

    private static void placeOrder() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("Only customers can place orders.");
            return;
        }

        List<MenuItem> menu = facade.getAvailableMenu();
        if (menu.isEmpty()) {
            System.out.println("No items available in the menu.");
            return;
        }

        System.out.println("\n--- Available Menu ---");
        System.out.printf("%-3s | %-5s | %-20s | %-10s | %s\n", "No", "ID", "Name", "Price", "Category");
        System.out.println("---------------------------------------------------------------");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            System.out.printf("%-3d | %-5s | %-20s | NPR %-7.2f | %s\n", 
                (i + 1), item.getItemId(), item.getName(), item.getPrice(), item.getCategory());
        }

        // Using a default restaurant for demo
        RestaurantOwner restaurant = UserFactory.createRestaurant("R001", "Priya Patel", "priya@email.com", "9841234567", "Kathmandu Pizza House");
        String orderId = "ORD" + (System.currentTimeMillis() % 100000);
        OrderBuilder builder = new OrderBuilder(orderId, (Customer) currentUser, restaurant);

        boolean addingItems = true;
        boolean hasItems = false;
        while (addingItems) {
            System.out.print("\nSelect item number to add (0 to finish): ");
            int itemChoice = getIntInput();
            if (itemChoice == 0) {
                addingItems = false;
            } else if (itemChoice > 0 && itemChoice <= menu.size()) {
                System.out.print("Quantity: ");
                int qty = getIntInput();
                if (qty > 0) {
                    builder.addItem(menu.get(itemChoice - 1), qty);
                    System.out.println("✓ Item added.");
                    hasItems = true;
                } else {
                    System.out.println("Quantity must be positive.");
                }
            } else {
                System.out.println("Invalid item choice.");
            }
        }

        if (!hasItems) {
            System.out.println("Order cancelled (no items selected).");
            return;
        }

        System.out.print("Delivery Address: ");
        String address = scanner.nextLine();
        builder.setDeliveryAddress(address);

        try {
            Order order = builder.build();
            System.out.println("\n--- Order Summary ---");
            System.out.println(order);
            System.out.println("Items:");
            for (OrderItem item : order.getItems()) {
                System.out.println("  - " + item);
            }
            System.out.printf("Total Amount: NPR %.2f\n", order.getFinalAmount());
            
            System.out.print("\nConfirm order and proceed to payment? (y/n): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("y")) {
                if (facade.placeOrder(order)) {
                    System.out.println("✓ Order placed and payment successful!");
                } else {
                    System.out.println("✗ Failed to place order.");
                }
            } else {
                System.out.println("Order discarded.");
            }
        } catch (Exception e) {
            System.out.println("Error building order: " + e.getMessage());
        }
    }

    private static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
    }

    private static void printBanner(String title) {
        String border = "=========================================";
        System.out.println("\n" + border);
        System.out.println(title);
        System.out.println(border + "\n");
    }
}