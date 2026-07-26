package com.foodordering.behavioral;

import com.foodordering.model.Order;

/**
 * Repository interface for Command pattern
 */
public interface OrderRepository {
    void save(Order order);
    void update(Order order);
    void delete(String orderId);
    Order findById(String orderId);
}
