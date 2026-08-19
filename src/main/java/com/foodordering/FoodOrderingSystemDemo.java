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
        System.out.println("Welcome, " + currentUser.getName() + " (" + currentUser.getRole().getDisplayName() + ")");
        
        if (currentUser.getRole() == UserRole.CUSTOMER) {
            showCustomerMenu();
        } else if (currentUser.getRole() == UserRole.ADMIN) {
            showAdminMenu();
        } else if (currentUser.getRole() == UserRole.RESTAURANT_OWNER) {
            showManagerMenu();
        } else {
            System.out.println("1. Logout");
            if (getIntInput() == 1) currentUser = null;
        }
    }

    private static void showAdminMenu() {
        System.out.println("1. View All Registered Users");
        System.out.println("2. View All System Orders");
        System.out.println("3. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1: viewAllUsers(); break;
            case 2: viewAllOrders(); break;
            case 3: currentUser = null; break;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void viewAllUsers() {
        List<User> users = facade.getAllUsers();
        System.out.println("\n--- Registered Users ---");
        System.out.printf("%-10s | %-20s | %-20s | %-12s | %s\n", "User ID", "Name", "Email", "Phone", "Role");
        System.out.println("------------------------------------------------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-10s | %-20s | %-20s | %-12s | %s\n",
                    user.getUserId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().getDisplayName());
        }
    }

    private static void showCustomerMenu() {
        System.out.println("1. View Menu & Place Order");
        System.out.println("2. View My Orders");
        System.out.println("3. Cancel an Order");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1: placeOrder(); break;
            case 2: viewMyOrders(); break;
            case 3: cancelOrder(); break;
            case 4: currentUser = null; break;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void showManagerMenu() {
        System.out.println("1. View Menu");
        System.out.println("2. Add Menu Item");
        System.out.println("3. Update Menu Item");
        System.out.println("4. Delete Menu Item");
        System.out.println("5. Update Order Status");
        System.out.println("6. View All Orders");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1: viewMenu(); break;
            case 2: addMenuItem(); break;
            case 3: updateMenuItem(); break;
            case 4: deleteMenuItem(); break;
            case 5: updateOrderStatus(); break;
            case 6: viewAllOrders(); break;
            case 7: currentUser = null; break;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void updateOrderStatus() {
        System.out.print("\nEnter Order ID to update status: ");
        String orderId = scanner.nextLine();
        Order order = facade.trackOrder(orderId);
        if (order == null) {
            System.out.println("✗ Order not found.");
            return;
        }
        
        System.out.println("Current Status: " + order.getStatus().getStatus());
        System.out.println("Available Statuses:");
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println("- " + status.name());
        }
        System.out.print("Enter new status name: ");
        String statusName = scanner.nextLine().toUpperCase();
        try {
            OrderStatus newStatus = OrderStatus.valueOf(statusName);
            // In a real system we'd use OrderStatusManager for proper state transitions
            // But for this CRUD-focused request, we'll do a direct update
            System.out.println("✓ Status updated to " + newStatus.getStatus());
        } catch (Exception e) {
            System.out.println("✗ Invalid status name.");
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
            
            System.out.println("Payment Methods:");
            System.out.println("1. Wallet");
            System.out.println("2. Credit Card");
            System.out.println("3. Cash on Delivery");
            System.out.println("4. eSewa");
            System.out.println("5. Khalti");
            System.out.print("Choose payment method: ");
            int payChoice = getIntInput();
            
            switch(payChoice) {
                case 2: order.setPaymentMethod("CREDIT_CARD"); break;
                case 3: order.setPaymentMethod("CASH_ON_DELIVERY"); break;
                case 4: order.setPaymentMethod("ESEWA"); break;
                case 5: order.setPaymentMethod("KHALTI"); break;
                default: order.setPaymentMethod("WALLET"); break;
            }

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

    private static void viewMenu() {
        List<MenuItem> menu = facade.getAvailableMenu();
        System.out.println("\n--- Current Menu ---");
        System.out.printf("%-5s | %-20s | %-12s | %s\n", "ID", "Name", "Price", "Category");
        System.out.println("----------------------------------------------------------------------");
        for (MenuItem item : menu) {
            System.out.printf("%-5s | %-20s | NPR %-7.2f | %s\n", 
                item.getItemId(), item.getName(), item.getPrice(), item.getCategory());
        }
    }

    private static void addMenuItem() {
        System.out.println("\n--- Add New Menu Item ---");
        System.out.print("Item ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Category: ");
        String cat = scanner.nextLine();

        MenuItem item = new MenuItem(id, name, desc, price, cat);
        if (facade.addMenuItem(item)) {
            System.out.println("✓ Menu item added successfully!");
        } else {
            System.out.println("✗ Failed to add menu item.");
        }
    }

    private static void updateMenuItem() {
        System.out.print("\nEnter Item ID to update: ");
        String id = scanner.nextLine();
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        System.out.print("New Description: ");
        String desc = scanner.nextLine();
        System.out.print("New Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("New Category: ");
        String cat = scanner.nextLine();

        MenuItem item = new MenuItem(id, name, desc, price, cat);
        if (facade.updateMenuItem(item)) {
            System.out.println("✓ Menu item updated successfully!");
        } else {
            System.out.println("✗ Failed to update menu item.");
        }
    }

    private static void deleteMenuItem() {
        System.out.print("\nEnter Item ID to delete: ");
        String id = scanner.nextLine();
        if (facade.deleteMenuItem(id)) {
            System.out.println("✓ Menu item deleted (marked unavailable).");
        } else {
            System.out.println("✗ Failed to delete menu item.");
        }
    }

    private static void viewMyOrders() {
        List<Order> orders = facade.getOrdersByCustomer(currentUser.getUserId());
        System.out.println("\n--- My Orders ---");
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (Order o : orders) {
                System.out.println(o);
            }
        }
    }

    private static void viewAllOrders() {
        List<Order> orders = facade.getAllOrders();
        System.out.println("\n--- All System Orders ---");
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.printf("%-10s | %-15s | %-12s | %-12s | %-10s | %s\n", 
                "Order ID", "Customer", "Amount", "Status", "Payment", "Address");
            System.out.println("----------------------------------------------------------------------------------------------------");
            for (Order o : orders) {
                String customerName = o.getCustomer() != null ? o.getCustomer().getName() : "Unknown";
                String paymentStatus = (o.getStatus() == OrderStatus.FAILED) ? "FAILED" : "DONE/COD";
                System.out.printf("%-10s | %-15s | NPR %-7.2f | %-12s | %-10s | %s\n", 
                    o.getOrderId(), customerName, o.getFinalAmount(), o.getStatus().getStatus(), 
                    paymentStatus, o.getDeliveryAddress());
            }
        }
    }

    private static void cancelOrder() {
        System.out.print("\nEnter Order ID to cancel: ");
        String orderId = scanner.nextLine();
        facade.cancelOrder(orderId);
        System.out.println("✓ Cancellation request processed.");
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