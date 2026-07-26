package com.foodordering.structural;

/**
 * Factory for creating Decorator instances
 */
public class OrderDecoratorFactory {
    
    public static OrderComponent createGiftWrapDecorator(OrderComponent order) {
        return new GiftWrapDecorator(order);
    }
    
    public static OrderComponent createPriorityDeliveryDecorator(OrderComponent order) {
        return new PriorityDeliveryDecorator(order);
    }
    
    public static OrderComponent createInsuranceDecorator(OrderComponent order) {
        return new InsuranceDecorator(order);
    }
    
    public static OrderComponent createLoyaltyDecorator(OrderComponent order, double discountPercentage) {
        return new LoyaltyDecorator(order, discountPercentage);
    }
}
