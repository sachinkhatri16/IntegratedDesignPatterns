package com.foodordering;

import com.foodordering.model.*;
import com.foodordering.creational.*;
import com.foodordering.behavioral.*;
import com.foodordering.structural.*;
import com.foodordering.service.*;

public class FoodOrderingSystemDemo {

    public static void main(String[] args) {
        printBanner("ONLINE FOOD ORDERING SYSTEM - DESIGN PATTERNS DEMO");

        // Initialize Singleton
        demoSingleton();

        // Factory Method Pattern
        demoFactoryMethod();

        // Builder Pattern
        demoBuilder();

        // Adapter Pattern
        demoAdapter();

        // Facade Pattern
        demoFacade();

        // Proxy Pattern
        demoProxy();

        // Decorator Pattern
        demoDecorator();

        // Strategy Pattern
        demoStrategy();

        // Observer Pattern
        demoObserver();

        // Command Pattern
        demoCommand();

        // State Pattern
        demoState();

        printBanner("SYSTEM DEMONSTRATION COMPLETE");
    }

    private static void demoSingleton() {
        System.out.println("=========================================");
        System.out.println("SINGLETON PATTERN - DATABASE MANAGER");
        System.out.println("=========================================");

        DatabaseManager db1 = DatabaseManager.getInstance();
        db1.connect();

        DatabaseManager db2 = DatabaseManager.getInstance();
        System.out.println("✓ db1 and db2 are same instance: " + (db1 == db2));

        System.out.println("✓ Connection String: " + db1.getConnectionString());
        System.out.println();
    }

    private static void demoFactoryMethod() {
        System.out.println("=========================================");
        System.out.println("FACTORY METHOD PATTERN - USER CREATION");
        System.out.println("=========================================");

        User customer = UserFactory.createUser("CUSTOMER", "C001", "Ramesh Adhikari", "ramesh@email.com", "9840123456");
        System.out.println("Created: " + customer);
        customer.displayRole();

        User restaurant = UserFactory.createRestaurant("R001", "Priya Patel", "priya@email.com", "9841234567", "Kathmandu Pizza House");
        System.out.println("Created: " + restaurant);
        restaurant.displayRole();

        User admin = UserFactory.createAdmin("A001", "Admin User", "admin@email.com", "9842345678", "SUPER");
        System.out.println("Created: " + admin);
        admin.displayRole();
        System.out.println();
    }

    private static void demoBuilder() {
        System.out.println("=========================================");
        System.out.println("BUILDER PATTERN - ORDER CONSTRUCTION");
        System.out.println("=========================================");

        Customer customer = UserFactory.createCustomer("C001", "Ramesh Adhikari", "ramesh@email.com", "9840123456");
        RestaurantOwner restaurant = UserFactory.createRestaurant("R001", "Priya Patel", "priya@email.com", "9841234567", "Kathmandu Pizza House");

        MenuItem pizza = new MenuItem("M1", "Margherita Pizza", "Fresh mozzarella and basil", 450.0, "Pizza");
        MenuItem coke = new MenuItem("M2", "Coca Cola", "Cold soft drink", 100.0, "Beverage");

        customer.setAddress("Thamel, Kathmandu");
        restaurant.verify();

        Order order = new OrderBuilder("ORD001", customer, restaurant)
                .addItem(pizza, 2)
                .addItem(coke, 2)
                .setDeliveryFee(50.0)
                .setDeliveryAddress("Thamel, Kathmandu")
                .setPaymentMethod("WALLET")
                .setSpecialInstructions("Extra cheese, no onions")
                .build();

        System.out.println("✓ Order built successfully: " + order);
        System.out.println("Items:");
        for (OrderItem item : order.getItems()) {
            System.out.println("  - " + item);
        }
        System.out.println("Total Amount: NPR " + String.format("%.2f", order.getFinalAmount()));
        System.out.println();
    }

    private static void demoAdapter() {
        System.out.println("=========================================");
        System.out.println("ADAPTER PATTERN - PAYMENT PROCESSING");
        System.out.println("=========================================");

        PaymentProcessor khaltiPayment = PaymentAdapterFactory.createKhaltiAdapter("9840123456");
        boolean result = khaltiPayment.processPayment(500.0, "9840123456");
        System.out.println("Payment Status: " + (result ? "Success" : "Failed"));

        System.out.println();

        PaymentProcessor esewaPayment = PaymentAdapterFactory.createEsewaAdapter("9841234567");
        result = esewaPayment.processPayment(750.0, "9841234567");
        System.out.println("Payment Status: " + (result ? "Success" : "Failed"));

        System.out.println();

        PaymentProcessor bankPayment = PaymentAdapterFactory.createBankTransferAdapter("12345678");
        result = bankPayment.processPayment(1000.0, "12345678");
        System.out.println("Payment Status: " + (result ? "Success" : "Failed"));
        System.out.println();
    }

    private static void demoFacade() {
        System.out.println("=========================================");
        System.out.println("FACADE PATTERN - UNIFIED ORDERING INTERFACE");
        System.out.println("=========================================");

        FoodOrderingFacade facade = new FoodOrderingFacade();

        System.out.println("✓ User login: " + facade.loginUser("C001", "password123"));
        System.out.println("✓ Customer registration: " + facade.registerCustomer("C002", "Suresh Kumar", "suresh@email.com", "9845678901"));
        System.out.println("✓ Searching restaurants: " + facade.searchRestaurants("Pizza"));
        System.out.println("✓ Getting restaurant menu: " + facade.getRestaurantMenu("R001"));
        System.out.println();
    }

    private static void demoProxy() {
        System.out.println("=========================================");
        System.out.println("PROXY PATTERN - CONTROLLED USER ACCESS");
        System.out.println("=========================================");

        UserProxy userProxy = new UserProxy("C001", "CUSTOMER");
        System.out.println("Attempting to access user without authorization...");

        try {
            User user = userProxy.getUser();
        } catch (SecurityException e) {
            System.out.println("✗ Access Denied: " + e.getMessage());
        }

        System.out.println("\nAuthorizing user...");
        userProxy.authorize();
        System.out.println("✓ User authorized");

        User user = userProxy.getUser();
        System.out.println("✓ User loaded: " + user);
        System.out.println();
    }

    private static void demoDecorator() {
        System.out.println("=========================================");
        System.out.println("DECORATOR PATTERN - DYNAMIC ORDER ENHANCEMENTS");
        System.out.println("=========================================");

        Customer customer = UserFactory.createCustomer("C001", "Ramesh", "ramesh@email.com", "9840123456");
        RestaurantOwner restaurant = UserFactory.createRestaurant("R001", "Priya", "priya@email.com", "9841234567", "Pizza House");

        MenuItem pizza = new MenuItem("M1", "Margherita", "Cheese pizza", 450.0, "Pizza");
        customer.setAddress("Thamel, Kathmandu");
        restaurant.verify();

        Order order = new OrderBuilder("ORD001", customer, restaurant)
                .addItem(pizza, 1)
                .setDeliveryFee(50.0)
                .setDeliveryAddress("Thamel")
                .build();

        OrderComponent basicOrder = new BasicOrder(order);
        System.out.println("Basic Order: " + basicOrder.getDescription());
        System.out.println("Price: NPR " + basicOrder.getTotalPrice());

        OrderComponent giftWrapped = OrderDecoratorFactory.createGiftWrapDecorator(basicOrder);
        System.out.println("\n✓ With Gift Wrap: " + giftWrapped.getDescription());
        System.out.println("Price: NPR " + giftWrapped.getTotalPrice());

        OrderComponent priority = OrderDecoratorFactory.createPriorityDeliveryDecorator(giftWrapped);
        System.out.println("\n✓ With Priority: " + priority.getDescription());
        System.out.println("Price: NPR " + priority.getTotalPrice());

        OrderComponent insurance = OrderDecoratorFactory.createInsuranceDecorator(priority);
        System.out.println("\n✓ With Insurance: " + insurance.getDescription());
        System.out.println("Price: NPR " + insurance.getTotalPrice());

        OrderComponent loyal = OrderDecoratorFactory.createLoyaltyDecorator(insurance, 10.0);
        System.out.println("\n✓ With Loyalty Discount: " + loyal.getDescription());
        System.out.println("Final Price: NPR " + loyal.getTotalPrice());
        System.out.println();
    }

    private static void demoStrategy() {
        System.out.println("=========================================");
        System.out.println("STRATEGY PATTERN - PAYMENT PROCESSING");
        System.out.println("=========================================");

        double orderAmount = 1250.0;

        PaymentStrategy walletPayment = PaymentStrategyFactory.createWalletStrategy(2000.0);
        System.out.println("Payment Method: Wallet");
        System.out.println("Amount: NPR " + orderAmount);
        walletPayment.pay(orderAmount);
        System.out.println("\nPayment Successful\n");

        PaymentStrategy creditCard = PaymentStrategyFactory.createCreditCardStrategy("4532111111111111", "Ramesh Adhikari");
        System.out.println("Payment Method: Credit Card");
        System.out.println("Amount: NPR " + orderAmount);
        creditCard.pay(orderAmount);
        System.out.println("\nPayment Successful\n");

        PaymentStrategy cod = PaymentStrategyFactory.createCashOnDeliveryStrategy("Delivery Agent");
        System.out.println("Payment Method: Cash on Delivery");
        System.out.println("Amount: NPR " + orderAmount);
        cod.pay(orderAmount);
        System.out.println("\nPayment Successful\n");

        PaymentStrategy digital = PaymentStrategyFactory.createDigitalWalletStrategy("Khalti", "9840123456");
        System.out.println("Payment Method: Khalti");
        System.out.println("Amount: NPR " + orderAmount);
        digital.pay(orderAmount);
        System.out.println("\nPayment Successful\n");
    }

    private static void demoObserver() {
        System.out.println("=========================================");
        System.out.println("OBSERVER PATTERN - ORDER NOTIFICATIONS");
        System.out.println("=========================================");

        Customer customer = UserFactory.createCustomer("C001", "Ramesh", "ramesh@email.com", "9840123456");
        RestaurantOwner restaurant = UserFactory.createRestaurant("R001", "Priya", "priya@email.com", "9841234567", "Pizza House");

        MenuItem pizza = new MenuItem("M1", "Pizza", "Cheese pizza", 450.0, "Pizza");
        customer.setAddress("Thamel");
        restaurant.verify();

        Order order = new OrderBuilder("ORD001", customer, restaurant)
                .addItem(pizza, 1)
                .setDeliveryFee(50.0)
                .setDeliveryAddress("Thamel")
                .build();

        NotificationService notificationService = new NotificationService();
        notificationService.setupNotifications(order, customer);

        System.out.println("Order Confirmed - Sending notifications:");
        notificationService.sendOrderConfirmation(order);

        System.out.println("\nOrder In Delivery - Sending notifications:");
        notificationService.sendDeliveryNotification(order);

        System.out.println("\nOrder Delivered - Sending notifications:");
        notificationService.sendDeliveryCompleteNotification(order);
        System.out.println();
    }

    private static void demoCommand() {
        System.out.println("=========================================");
        System.out.println("COMMAND PATTERN - ORDER OPERATIONS");
        System.out.println("=========================================");

        Customer customer = UserFactory.createCustomer("C001", "Ramesh", "ramesh@email.com", "9840123456");
        RestaurantOwner restaurant = UserFactory.createRestaurant("R001", "Priya", "priya@email.com", "9841234567", "Pizza House");

        MenuItem pizza = new MenuItem("M1", "Pizza", "Cheese pizza", 450.0, "Pizza");
        customer.setAddress("Thamel");
        restaurant.verify();

        Order order = new OrderBuilder("ORD001", customer, restaurant)
                .addItem(pizza, 1)
                .setDeliveryFee(50.0)
                .setDeliveryAddress("Thamel")
                .build();

        OrderService orderService = new OrderService();

        System.out.println("Placing order...");
        orderService.placeOrder(order);

        System.out.println("\nOrder placed successfully!");
        System.out.println("Order Status: " + order.getStatus().getStatus());
        System.out.println();
    }

    private static void demoState() {
        System.out.println("=========================================");
        System.out.println("STATE PATTERN - ORDER LIFECYCLE");
        System.out.println("=========================================");

        OrderContext orderContext = new OrderContext();

        System.out.println("Current Status: " + orderContext.getCurrentStatus());

        System.out.println("\nAttempting to prepare order (should fail):");
        orderContext.prepare();

        System.out.println("\nConfirming order:");
        orderContext.confirm();
        System.out.println("Current Status: " + orderContext.getCurrentStatus());

        System.out.println("\nPreparing order:");
        orderContext.prepare();
        System.out.println("Current Status: " + orderContext.getCurrentStatus());

        System.out.println("\nMarking as ready:");
        orderContext.markReady();
        System.out.println("Current Status: " + orderContext.getCurrentStatus());

        System.out.println("\nDispatching for delivery:");
        orderContext.deliver();
        System.out.println("Current Status: " + orderContext.getCurrentStatus());
        System.out.println();
    }

    private static void printBanner(String title) {
        String border = "=========================================";
        System.out.println("\n" + border);
        System.out.println(title);
        System.out.println(border + "\n");
    }
}