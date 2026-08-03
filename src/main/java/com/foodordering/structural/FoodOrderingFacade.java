package com.foodordering.structural;

import com.foodordering.model.Order;
import com.foodordering.model.MenuItem;
import com.foodordering.model.User;
import com.foodordering.model.UserRole;
import com.foodordering.creational.DatabaseManager;
import com.foodordering.creational.UserFactory;
import java.sql.*;
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

    public boolean registerCustomer(String userId, String name, String email, String phone, String password) {
        return userRepository.registerCustomer(userId, name, email, phone, password);
    }

    public List<MenuItem> getAvailableMenu() {
        return restaurantRepository.getAllMenuItems();
    }

    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public List<Object> searchRestaurants(String cuisine) {
        return restaurantRepository.searchByType(cuisine);
    }

    public List<Object> getRestaurantMenu(String restaurantId) {
        return restaurantRepository.getMenu(restaurantId);
    }

    public boolean placeOrder(Order order) {
        if (order != null && paymentService.processPayment(order)) {
            orderRepository.saveOrder(order);
            notificationService.sendOrderConfirmation(order);
            return true;
        }
        return false;
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
            String sql = "SELECT * FROM users WHERE userId = ? AND password = ?";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, userId);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();
                    return rs.next();
                }
            } catch (SQLException e) {
                System.err.println("✗ Auth error: " + e.getMessage());
                return false;
            }
        }

        public boolean registerCustomer(String userId, String name, String email, String phone, String password) {
            String sql = "INSERT INTO users(userId, name, email, phone, role, password, active) VALUES(?, ?, ?, ?, 'CUSTOMER', ?, 1)";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, userId);
                    pstmt.setString(2, name);
                    pstmt.setString(3, email);
                    pstmt.setString(4, phone);
                    pstmt.setString(5, password);
                    pstmt.executeUpdate();
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("✗ Registration error: " + e.getMessage());
                return false;
            }
        }

        public User getUser(String userId) {
            String sql = "SELECT * FROM users WHERE userId = ?";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, userId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String role = rs.getString("role");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String phone = rs.getString("phone");
                        return UserFactory.createUser(role, userId, name, email, phone);
                    }
                }
            } catch (SQLException e) {
                System.err.println("✗ Get user error: " + e.getMessage());
            }
            return null;
        }
    }

    static class RestaurantRepository {
        public List<MenuItem> getAllMenuItems() {
            List<MenuItem> items = new ArrayList<>();
            String sql = "SELECT * FROM menu_items WHERE available = 1";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        items.add(new MenuItem(
                                rs.getString("itemId"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getDouble("price"),
                                rs.getString("category")
                        ));
                    }
                }
            } catch (SQLException e) {
                System.err.println("✗ Get menu error: " + e.getMessage());
            }
            return items;
        }

        public List<Object> searchByType(String cuisine) {
            // In a real system, this would search restaurants.
            // For now, let's keep it simple as per original mock.
            return new ArrayList<>();
        }
        public List<Object> getMenu(String restaurantId) {
            return new ArrayList<>();
        }
    }

    static class OrderRepository {
        public void saveOrder(Order order) {
            String sqlOrder = "INSERT INTO orders(orderId, customerId, restaurantId, amount, status, deliveryAddress) VALUES(?, ?, ?, ?, ?, ?)";
            String sqlItem = "INSERT INTO order_items(orderId, itemId, quantity, price) VALUES(?, ?, ?, ?)";
            
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                conn.setAutoCommit(false);
                try (PreparedStatement pstmtOrder = conn.prepareStatement(sqlOrder);
                     PreparedStatement pstmtItem = conn.prepareStatement(sqlItem)) {
                    
                    pstmtOrder.setString(1, order.getOrderId());
                    pstmtOrder.setString(2, order.getCustomer().getUserId());
                    pstmtOrder.setString(3, order.getRestaurant().getUserId());
                    pstmtOrder.setDouble(4, order.getFinalAmount());
                    pstmtOrder.setString(5, order.getStatus().getStatus());
                    pstmtOrder.setString(6, order.getDeliveryAddress());
                    pstmtOrder.executeUpdate();

                    for (com.foodordering.model.OrderItem item : order.getItems()) {
                        pstmtItem.setString(1, order.getOrderId());
                        pstmtItem.setString(2, item.getMenuItem().getItemId());
                        pstmtItem.setInt(3, item.getQuantity());
                        pstmtItem.setDouble(4, item.getUnitPrice());
                        pstmtItem.executeUpdate();
                    }
                    
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("✗ Save order error: " + e.getMessage());
            }
        }

        public Order getOrder(String orderId) {
            // Simplified for demo
            return null; 
        }

        public void cancelOrder(String orderId) {
            String sql = "UPDATE orders SET status = 'CANCELLED' WHERE orderId = ?";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, orderId);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("✗ Cancel order error: " + e.getMessage());
            }
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
