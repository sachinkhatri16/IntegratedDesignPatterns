package com.foodordering;

import com.foodordering.model.*;
import com.foodordering.creational.*;
import com.foodordering.behavioral.*;
import com.foodordering.structural.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test Cases for Behavioral Design Patterns
 */
public class BehavioralPatternTests {

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
    public void testStrategyPatternWalletPayment() {
        PaymentStrategy strategy = PaymentStrategyFactory.createWalletStrategy(1000.0);
        assertTrue("Payment should succeed with sufficient balance", strategy.pay(500.0));
        assertEquals("Payment method should be WALLET", "WALLET", strategy.getPaymentMethodName());
    }

    @Test
    public void testStrategyPatternInsufficientBalance() {
        PaymentStrategy strategy = PaymentStrategyFactory.createWalletStrategy(100.0);
        assertFalse("Payment should fail with insufficient balance", strategy.pay(500.0));
    }

    @Test
    public void testStrategyPatternCreditCard() {
        PaymentStrategy strategy = PaymentStrategyFactory.createCreditCardStrategy("4532111111111111", "Ramesh");
        assertTrue("Credit card payment should succeed", strategy.pay(500.0));
        assertEquals("Payment method should be CREDIT_CARD", "CREDIT_CARD", strategy.getPaymentMethodName());
    }

    @Test
    public void testStrategyPatternEsewaPayment() {
        PaymentStrategy strategy = PaymentStrategyFactory.createEsewaStrategy("esewa_user_123");
        assertTrue("eSewa payment should succeed", strategy.pay(500.0));
        assertEquals("Payment method should be ESEWA", "ESEWA", strategy.getPaymentMethodName());
    }

    @Test
    public void testStrategyPatternKhaltiPayment() {
        PaymentStrategy strategy = PaymentStrategyFactory.createKhaltiStrategy("khalti_user_456");
        assertTrue("Khalti payment should succeed", strategy.pay(500.0));
        assertEquals("Payment method should be KHALTI", "KHALTI", strategy.getPaymentMethodName());
    }

    @Test
    public void testStatePatternExtendedOrderLifecycle() {
        OrderContext context = new OrderContext();
        
        assertEquals("Initial status should be PENDING", "PENDING", context.getCurrentStatus());
        
        context.confirm();
        assertEquals("Status should be CONFIRMED", "CONFIRMED", context.getCurrentStatus());
        
        context.prepare();
        assertEquals("Status should be PREPARING", "PREPARING", context.getCurrentStatus());
        
        context.markReady();
        assertEquals("Status should be READY", "READY", context.getCurrentStatus());

        context.pickUp();
        assertEquals("Status should be PICKED_UP", "PICKED_UP", context.getCurrentStatus());

        context.setOnWay();
        assertEquals("Status should be ON_THE_WAY", "ON_THE_WAY", context.getCurrentStatus());
        
        context.deliver();
        assertEquals("Status should be DELIVERED", "DELIVERED", context.getCurrentStatus());
    }

    @Test
    public void testCommandPatternOrderUpdate() {
        MockOrderRepository repo = new MockOrderRepository();
        MenuItem newItem = new MenuItem("M2", "Burger", "Burger", 300.0, "Fast Food");
        OrderItem orderItem = new OrderItem(newItem, 2);
        java.util.List<OrderItem> newItems = java.util.Arrays.asList(orderItem);

        OrderCommand command = OrderCommandFactory.createUpdateOrderCommand(order, newItems, repo);
        
        double originalAmount = order.getTotalAmount();
        command.execute();
        
        assertEquals("Total amount should be updated", 600.0, order.getTotalAmount(), 0.01);
        assertEquals("Items count should be 1", 1, order.getItems().size());
        
        command.undo();
        assertEquals("Total amount should be restored", originalAmount, order.getTotalAmount(), 0.01);
    }

    @Test
    public void testStatePatternCancelAfterReady() {
        OrderContext context = new OrderContext();
        context.confirm();
        context.prepare();
        context.markReady();
        
        context.cancel();
        assertEquals("Status should be CANCELLED", "CANCELLED", context.getCurrentStatus());
    }

    @Test
    public void testStatePatternInvalidTransition() {
        OrderContext context = new OrderContext();
        
        // Try to prepare before confirming
        context.prepare(); // Should print error message
        assertEquals("Status should still be PENDING", "PENDING", context.getCurrentStatus());
    }

    @Test
    public void testObserverPatternOrderStatusNotification() {
        OrderStatusManager manager = new OrderStatusManager(order);
        
        OrderObserver emailObserver = NotificationObserverFactory.createEmailObserver("ramesh@email.com");
        OrderObserver smsObserver = NotificationObserverFactory.createSMSObserver("9840123456");
        
        manager.attach(emailObserver);
        manager.attach(smsObserver);
        
        manager.updateStatus(OrderStatus.CONFIRMED);
        
        assertEquals("Order status should be CONFIRMED", OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    public void testCommandPatternOrderPlacement() {
        MockOrderRepository repo = new MockOrderRepository();
        OrderCommand command = OrderCommandFactory.createPlaceOrderCommand(order, repo);
        
        assertEquals("Initial status should be PENDING", OrderStatus.PENDING, order.getStatus());
        
        command.execute();
        assertEquals("Status should be CONFIRMED after execution", OrderStatus.CONFIRMED, order.getStatus());
        
        command.undo();
        // After undo, status should be restored
    }

    private static class MockOrderRepository implements OrderRepository {
        @Override
        public void save(Order order) {}
        @Override
        public void update(Order order) {}
        @Override
        public void delete(String orderId) {}
        @Override
        public Order findById(String orderId) { return null; }
    }
}
