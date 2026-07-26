package com.foodordering;

import com.foodordering.model.*;
import com.foodordering.creational.*;
import com.foodordering.structural.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test Cases for Structural Design Patterns
 */
public class StructuralPatternTests {

    private Order order;
    private Customer customer;
    private RestaurantOwner restaurant;

    @Before
    public void setUp() {
        customer = UserFactory.createCustomer("C001", "Ramesh", "ramesh@email.com", "9840123456");
        restaurant = UserFactory.createRestaurant("R001", "Priya", "priya@email.com", "9841234567", "Pizza House");
        customer.setAddress("Thamel");
        restaurant.verify();
        
        MenuItem item = new MenuItem("M1", "Pizza", "Pizza", 450.0, "Pizza");
        order = new OrderBuilder("ORD001", customer, restaurant)
                .addItem(item, 1)
                .setDeliveryFee(50.0)
                .setDeliveryAddress("Thamel")
                .build();
    }

    @Test
    public void testAdapterPatternKhaltiPayment() {
        PaymentProcessor khalti = PaymentAdapterFactory.createKhaltiAdapter("9840123456");
        
        assertTrue("Khalti payment should process", khalti.processPayment(500.0, "9840123456"));
        assertEquals("Payment method should be KHALTI", "KHALTI", khalti.getPaymentMethodName());
    }

    @Test
    public void testAdapterPatternEsewaPayment() {
        PaymentProcessor esewa = PaymentAdapterFactory.createEsewaAdapter("9841234567");
        
        assertTrue("eSewa payment should process", esewa.processPayment(750.0, "9841234567"));
        assertEquals("Payment method should be ESEWA", "ESEWA", esewa.getPaymentMethodName());
    }

    @Test
    public void testAdapterPatternBankPayment() {
        PaymentProcessor bank = PaymentAdapterFactory.createBankTransferAdapter("123456789");
        
        assertTrue("Bank transfer should process", bank.processPayment(1000.0, "123456789"));
        assertEquals("Payment method should be BANK_TRANSFER", "BANK_TRANSFER", bank.getPaymentMethodName());
    }

    @Test
    public void testProxyPatternUnauthorizedAccess() {
        UserProxy userProxy = new UserProxy("C001", "CUSTOMER");
        
        try {
            User user = userProxy.getUser();
            fail("Should throw SecurityException for unauthorized access");
        } catch (SecurityException e) {
            assertTrue("Exception should mention unauthorized", e.getMessage().contains("not authorized"));
        }
    }

    @Test
    public void testProxyPatternAuthorizedAccess() {
        UserProxy userProxy = new UserProxy("C001", "CUSTOMER");
        
        userProxy.authorize();
        User user = userProxy.getUser();
        
        assertNotNull("User should be loaded", user);
        assertTrue("User should be a Customer", user instanceof Customer);
    }

    @Test
    public void testProxyPatternLazyLoading() {
        UserProxy userProxy = new UserProxy("C001", "CUSTOMER");
        userProxy.authorize();
        
        // First access - should load from database
        User user1 = userProxy.getUser();
        
        // Second access - should return cached instance
        User user2 = userProxy.getUser();
        
        assertSame("Both calls should return same instance", user1, user2);
    }

    @Test
    public void testDecoratorPatternBasicOrder() {
        OrderComponent basicOrder = new BasicOrder(order);
        
        assertTrue("Description should contain Order ID", basicOrder.getDescription().contains("ORD001"));
        double basePrice = basicOrder.getTotalPrice();
        assertTrue("Price should equal order total", basePrice > 0);
    }

    @Test
    public void testDecoratorPatternGiftWrap() {
        OrderComponent basicOrder = new BasicOrder(order);
        OrderComponent wrapped = OrderDecoratorFactory.createGiftWrapDecorator(basicOrder);
        
        assertTrue("Description should mention Gift Wrap", wrapped.getDescription().contains("Gift Wrap"));
        assertEquals("Price should increase by 50", basicOrder.getTotalPrice() + 50.0, wrapped.getTotalPrice(), 0.1);
    }

    @Test
    public void testDecoratorPatternMultipleDecorators() {
        OrderComponent basicOrder = new BasicOrder(order);
        OrderComponent wrapped = OrderDecoratorFactory.createGiftWrapDecorator(basicOrder);
        OrderComponent priority = OrderDecoratorFactory.createPriorityDeliveryDecorator(wrapped);
        OrderComponent insurance = OrderDecoratorFactory.createInsuranceDecorator(priority);
        
        double expectedPrice = basicOrder.getTotalPrice() + 50.0 + 100.0 + 25.0;
        assertEquals("Price should reflect all decorators", expectedPrice, insurance.getTotalPrice(), 0.1);
    }

    @Test
    public void testDecoratorPatternLoyaltyDiscount() {
        OrderComponent basicOrder = new BasicOrder(order);
        OrderComponent withDiscount = OrderDecoratorFactory.createLoyaltyDecorator(basicOrder, 10.0);
        
        double basePrice = basicOrder.getTotalPrice();
        double discountedPrice = withDiscount.getTotalPrice();
        double expected = basePrice * 0.9;
        
        assertEquals("Price should be reduced by 10%", expected, discountedPrice, 0.1);
    }
}
