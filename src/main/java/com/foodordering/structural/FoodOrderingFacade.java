package com.foodordering.structural;

import com.foodordering.model.Order;
import java.util.ArrayList;
import java.util.List;

/**
 * FACADE PATTERN - FoodOrderingFacade
 * Provides simplified unified interface to the complex subsystems
 */
public class FoodOrderingFacade {
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private OrderRepository orderRepository;
    private NotificationService notificationService;
    private PaymentService paymentService;

    public FoodOrderingFacade() {
        this.userRepository = new UserRepository();
        this.restaurantRepository = new RestaurantRepository();
        this.orderRepository = new OrderRepository();
        this.notificationService = new NotificationService();
        this.paymentService = new PaymentService();
    }

    public boolean loginUser(String userId, String password) {
        return userRepository.authenticate(userId, password);
    }

    public boolean registerCustomer(String userId, String name, String email, String phone) {
        return userRepository.registerCustomer(userId, name, email, phone);
    }

    public List<Object> searchRestaurants(String cuisine) {
        return restaurantRepository.searchByType(cuisine);
    }

    public List<Object> getRestaurantMenu(String restaurantId) {
        return restaurantRepository.getMenu(restaurantId);
    }

    public boolean placeOrder(String orderId, String customerId, String restaurantId) {
        Order order = orderRepository.getOrder(orderId);
        if (order != null && paymentService.processPayment(order)) {
            orderRepository.saveOrder(order);
            notificationService.sendOrderConfirmation(order);
            return true;
        }
        return false;
    }

    public Order trackOrder(String orderId) {
        return orderRepository.getOrder(orderId);
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order != null) {
            orderRepository.cancelOrder(orderId);
            notificationService.sendCancellationNotification(order);
        }
    }

    // Internal subsystems
    static class UserRepository {
        public boolean authenticate(String userId, String password) {
            return true;
        }
        public boolean registerCustomer(String userId, String name, String email, String phone) {
            return true;
        }
    }

    static class RestaurantRepository {
        public List<Object> searchByType(String cuisine) {
            return new ArrayList<>();
        }
        public List<Object> getMenu(String restaurantId) {
            return new ArrayList<>();
        }
    }

    static class OrderRepository {
        private List<Order> orders = new ArrayList<>();
        public Order getOrder(String orderId) {
            return orders.stream().filter(o -> o.getOrderId().equals(orderId)).findFirst().orElse(null);
        }
        public void saveOrder(Order order) {
            orders.add(order);
        }
        public void cancelOrder(String orderId) {
            orders.removeIf(o -> o.getOrderId().equals(orderId));
        }
    }

    static class NotificationService {
        public void sendOrderConfirmation(Order order) {
            System.out.println("  → Confirmation notification sent to " + order.getCustomer().getEmail());
        }
        public void sendCancellationNotification(Order order) {
            System.out.println("  → Cancellation notification sent to " + order.getCustomer().getEmail());
        }
    }

    static class PaymentService {
        public boolean processPayment(Order order) {
            return true;
        }
    }
}
