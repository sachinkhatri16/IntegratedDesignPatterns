package com.foodordering;

import com.foodordering.model.*;
import com.foodordering.creational.*;
import com.foodordering.behavioral.*;
import com.foodordering.structural.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test Cases for Creational Design Patterns
 */
public class CreationalPatternTests {

    @Test
    public void testSingletonDatabaseManager() {
        DatabaseManager db1 = DatabaseManager.getInstance();
        DatabaseManager db2 = DatabaseManager.getInstance();
        
        assertSame("Singleton instances should be identical", db1, db2);
        assertTrue("Database should be connectable", db1.getConnectionString().contains("localhost"));
    }

    @Test
    public void testFactoryMethodUserCreation() {
        User customer = UserFactory.createUser("CUSTOMER", "C001", "Ramesh", "ramesh@email.com", "9840123456");
        User restaurant = UserFactory.createUser("RESTAURANT", "R001", "Priya", "priya@email.com", "9841234567");
        User admin = UserFactory.createUser("ADMIN", "A001", "Admin", "admin@email.com", "9842345678");
        
        assertTrue("Customer should be instanceof Customer", customer instanceof Customer);
        assertTrue("Restaurant should be instanceof RestaurantOwner", restaurant instanceof RestaurantOwner);
        assertTrue("Admin should be instanceof Administrator", admin instanceof Administrator);
    }

    @Test
    public void testFactoryMethodInvalidUserType() {
        try {
            UserFactory.createUser("INVALID", "U001", "Name", "email@com", "98xxxxxxxx");
            fail("Should throw IllegalArgumentException for invalid user type");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message should mention Invalid user type", e.getMessage().contains("Invalid user type"));
        }
    }

    @Test
    public void testBuilderPatternOrderConstruction() {
        Customer customer = UserFactory.createCustomer("C001", "Ramesh", "ramesh@email.com", "9840123456");
        RestaurantOwner restaurant = UserFactory.createRestaurant("R001", "Priya", "priya@email.com", "9841234567", "Pizza House");
        
        customer.setAddress("Thamel");
        restaurant.verify();
        
        MenuItem item1 = new MenuItem("M1", "Pizza", "Cheese pizza", 450.0, "Pizza");
        MenuItem item2 = new MenuItem("M2", "Coke", "Soft drink", 100.0, "Beverage");
        
        Order order = new OrderBuilder("ORD001", customer, restaurant)
                .addItem(item1, 2)
                .addItem(item2, 1)
                .setDeliveryFee(50.0)
                .setDeliveryAddress("Thamel")
                .setPaymentMethod("WALLET")
                .build();
        
        assertEquals("Order should have 2 items", 2, order.getItems().size());
        assertEquals("Order ID should match", "ORD001", order.getOrderId());
        assertTrue("Total should include delivery fee", order.getFinalAmount() > order.getTotalAmount());
    }

    @Test
    public void testBuilderPatternMissingRequiredField() {
        Customer customer = UserFactory.createCustomer("C001", "Ramesh", "ramesh@email.com", "9840123456");
        RestaurantOwner restaurant = UserFactory.createRestaurant("R001", "Priya", "priya@email.com", "9841234567", "Pizza House");
        MenuItem item = new MenuItem("M1", "Pizza", "Pizza", 450.0, "Pizza");
         
        restaurant.verify();
         
        try {
            Order order = new OrderBuilder("ORD001", customer, restaurant)
                    .addItem(item, 1)
                    // Missing delivery address
                    .build();
            fail("Should throw IllegalStateException for missing delivery address");
        } catch (IllegalStateException e) {
            assertTrue("Exception should mention delivery address", 
                      e.getMessage().toLowerCase().contains("delivery address"));
        }
    }
}
