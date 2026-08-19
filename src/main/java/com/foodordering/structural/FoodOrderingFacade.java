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
        orderRepository.cancelOrder(orderId);
        if (order != null) {
            notificationService.sendCancellationNotification(order);
        }
    }

    public boolean addMenuItem(MenuItem item) {
        return restaurantRepository.addMenuItem(item);
    }

    public boolean updateMenuItem(MenuItem item) {
        return restaurantRepository.updateMenuItem(item);
    }

    public boolean deleteMenuItem(String itemId) {
        return restaurantRepository.deleteMenuItem(itemId);
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.getOrdersByCustomer(customerId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
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

        public List<User> getAllUsers() {
            List<User> users = new ArrayList<>();
            String sql = "SELECT * FROM users ORDER BY name";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        String role = rs.getString("role");
                        String userId = rs.getString("userId");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String phone = rs.getString("phone");
                        users.add(UserFactory.createUser(role, userId, name, email, phone));
                    }
                }
            } catch (SQLException e) {
                System.err.println("✗ Get all users error: " + e.getMessage());
            }
            return users;
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

        public boolean addMenuItem(MenuItem item) {
            String sql = "INSERT INTO menu_items(itemId, name, description, price, category, available) VALUES(?, ?, ?, ?, ?, 1)";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, item.getItemId());
                    pstmt.setString(2, item.getName());
                    pstmt.setString(3, item.getDescription());
                    pstmt.setDouble(4, item.getPrice());
                    pstmt.setString(5, item.getCategory());
                    return pstmt.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println("✗ Add menu item error: " + e.getMessage());
                return false;
            }
        }

        public boolean updateMenuItem(MenuItem item) {
            String sql = "UPDATE menu_items SET name = ?, description = ?, price = ?, category = ? WHERE itemId = ?";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, item.getName());
                    pstmt.setString(2, item.getDescription());
                    pstmt.setDouble(3, item.getPrice());
                    pstmt.setString(4, item.getCategory());
                    pstmt.setString(5, item.getItemId());
                    return pstmt.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println("✗ Update menu item error: " + e.getMessage());
                return false;
            }
        }

        public boolean deleteMenuItem(String itemId) {
            String sql = "UPDATE menu_items SET available = 0 WHERE itemId = ?";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, itemId);
                    return pstmt.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println("✗ Delete menu item error: " + e.getMessage());
                return false;
            }
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
            String sql = "SELECT o.*, u.name as customerName, u.email as customerEmail, u.phone as customerPhone FROM orders o JOIN users u ON o.customerId = u.userId WHERE o.orderId = ?";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, orderId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            User user = UserFactory.createUser("CUSTOMER", rs.getString("customerId"), rs.getString("customerName"), rs.getString("customerEmail"), rs.getString("customerPhone"));
                            com.foodordering.model.Customer customer = (com.foodordering.model.Customer) user;
                            Order order = new Order(rs.getString("orderId"), customer, null); // Simplified restaurant
                            order.setDeliveryAddress(rs.getString("deliveryAddress"));
                            String statusStr = rs.getString("status").toUpperCase().replace(" ", "_");
                            try {
                                order.setStatus(com.foodordering.model.OrderStatus.valueOf(statusStr));
                            } catch (Exception e) {
                                // Fallback if status string doesn't match enum exactly
                                order.setStatus(com.foodordering.model.OrderStatus.PENDING);
                            }
                            return order;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("✗ Get order error: " + e.getMessage());
            }
            return null;
        }

        public List<Order> getOrdersByCustomer(String customerId) {
            List<Order> orders = new ArrayList<>();
            String sql = "SELECT * FROM orders WHERE customerId = ? ORDER BY orderId DESC";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, customerId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            Order order = new Order(rs.getString("orderId"), null, null);
                            String statusStr = rs.getString("status").toUpperCase().replace(" ", "_");
                            try {
                                order.setStatus(com.foodordering.model.OrderStatus.valueOf(statusStr));
                            } catch (Exception e) {
                                order.setStatus(com.foodordering.model.OrderStatus.PENDING);
                            }
                            order.setDeliveryAddress(rs.getString("deliveryAddress"));
                            orders.add(order);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("✗ Get orders error: " + e.getMessage());
            }
            return orders;
        }

        public List<Order> getAllOrders() {
            List<Order> orders = new ArrayList<>();
            String sql = "SELECT o.*, u.name as customerName FROM orders o LEFT JOIN users u ON o.customerId = u.userId ORDER BY o.orderId DESC";
            try {
                Connection conn = DatabaseManager.getInstance().getConnection();
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        User customer = null;
                        if (rs.getString("customerName") != null) {
                            customer = UserFactory.createUser("CUSTOMER", rs.getString("customerId"), rs.getString("customerName"), "", "");
                        }
                        Order order = new Order(rs.getString("orderId"), (com.foodordering.model.Customer) customer, null);
                        String statusStr = rs.getString("status").toUpperCase().replace(" ", "_");
                        try {
                            order.setStatus(com.foodordering.model.OrderStatus.valueOf(statusStr));
                        } catch (Exception e) {
                            order.setStatus(com.foodordering.model.OrderStatus.PENDING);
                        }
                        order.setDeliveryAddress(rs.getString("deliveryAddress"));
                        orders.add(order);
                    }
                }
            } catch (Exception e) {
                System.err.println("✗ Get all orders error: " + e.getMessage());
            }
            return orders;
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
            System.out.println("  📧 Email: Order #" + order.getOrderId() + " confirmed for " + order.getCustomer().getName());
        }
        public void sendCancellationNotification(Order order) {
            System.out.println("  📧 Email: Order #" + order.getOrderId() + " has been cancelled.");
        }
        public void sendStatusUpdate(Order order) {
            System.out.println("  🔔 Notification: Order #" + order.getOrderId() + " is now " + order.getStatus().getStatus());
        }
    }

    static class PaymentService {
        public boolean processPayment(Order order) {
            return true;
        }
    }
}
