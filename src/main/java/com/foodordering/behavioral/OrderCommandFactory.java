package com.foodordering.behavioral;

/**
 * Factory for creating Command instances
 */
public class OrderCommandFactory {
    
    public static OrderCommand createPlaceOrderCommand(com.foodordering.model.Order order, OrderRepository repository) {
        return new PlaceOrderCommand(order, repository);
    }
    
    public static OrderCommand createCancelOrderCommand(com.foodordering.model.Order order, OrderRepository repository) {
        return new CancelOrderCommand(order, repository);
    }
    
    public static OrderCommand createDeliverOrderCommand(com.foodordering.model.Order order, OrderRepository repository) {
        return new DeliverOrderCommand(order, repository);
    }

    public static OrderCommand createUpdateOrderCommand(com.foodordering.model.Order order, java.util.List<com.foodordering.model.OrderItem> newItems, OrderRepository repository) {
        return new UpdateOrderCommand(order, newItems, repository);
    }
}
