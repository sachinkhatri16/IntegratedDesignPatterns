package com.foodordering.creational;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * SINGLETON PATTERN - DatabaseManager
 * Ensures only one instance of database connection throughout the application
 */
public class DatabaseManager {
    // Make singleton safe
    private static volatile DatabaseManager instance;
    private static final Object lock = new Object();
    public static final String DEFAULT_DB_PASSWORD = "1928374650@Asd";

    private String connectionString;
    private Connection connection;
    private String username;
    private String password;

    private DatabaseManager() {
        // Read from environment, fallback to local defaults
        this.connectionString = System.getenv().getOrDefault(
                "DB_URL",
                "jdbc:postgresql://localhost:5432/food_ordering"
        );
        this.username = System.getenv().getOrDefault("DB_USER", "postgres");
        // Read password from DB_PASSWORD environment variable, falling back to the local demo password
        this.password = System.getenv().getOrDefault("DB_PASSWORD", DEFAULT_DB_PASSWORD);
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public Connection connect() {
        try {
            // Ensure driver is loaded
            Class.forName("org.postgresql.Driver");

            if (connection == null || connection.isClosed()) {
                connection = openConnection(password);
                System.out.println("✓ Database connected: " + connectionString);
                initializeDatabase();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ PostgreSQL driver not found: " + e.getMessage());
        } catch (SQLException e) {
            if (shouldRetryWithFallback(e)) {
                try {
                    connection = openConnection(DEFAULT_DB_PASSWORD);
                    password = DEFAULT_DB_PASSWORD;
                    System.out.println("✓ Database connected with built-in demo password: " + connectionString);
                    initializeDatabase();
                    return connection;
                } catch (SQLException retryException) {
                    System.err.println("✗ Fallback connection failed: " + retryException.getMessage());
                }
            }
            System.err.println("✗ Connection failed: " + e.getMessage());
        }

        if (connection == null) {
            System.err.println("✗ Connection is null. Troubleshooting:");
            System.err.println("  - Ensure PostgreSQL is running and listening on the host/port in DB_URL (default: localhost:5432)");
            System.err.println("  - Verify the database exists (e.g. 'food_ordering') and the user/password are correct");
            System.err.println("  - Set environment variables: DB_URL, DB_USER, DB_PASSWORD before starting the app");
            System.err.println("  - Make sure the PostgreSQL JDBC driver is on the runtime classpath (org.postgresql:postgresql in pom.xml)");
        }

        return connection;
    }

    private Connection openConnection(String passwordToUse) throws SQLException {
        if (passwordToUse == null || passwordToUse.isBlank()) {
            return DriverManager.getConnection(connectionString, username, "");
        }
        return DriverManager.getConnection(connectionString, username, passwordToUse);
    }

    private boolean shouldRetryWithFallback(SQLException exception) {
        String message = exception.getMessage();
        return message != null
                && message.toLowerCase().contains("password authentication failed")
                && password != null
                && !password.isBlank()
                && !DEFAULT_DB_PASSWORD.equals(password);
    }

    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Create Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "userId TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "phone TEXT, " +
                    "role TEXT, " +
                    "password TEXT, " +
                    "active INTEGER)");

            // Create MenuItems table
            stmt.execute("CREATE TABLE IF NOT EXISTS menu_items (" +
                    "itemId TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "description TEXT, " +
                    "price REAL, " +
                    "category TEXT, " +
                    "available INTEGER)");

            // Create Orders table
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "orderId TEXT PRIMARY KEY, " +
                    "customerId TEXT, " +
                    "restaurantId TEXT, " +
                    "amount REAL, " +
                    "status TEXT, " +
                    "deliveryAddress TEXT, " +
                    "FOREIGN KEY(customerId) REFERENCES users(userId))");

            // Create OrderItems table
            stmt.execute("CREATE TABLE IF NOT EXISTS order_items (" +
                    "orderId TEXT, " +
                    "itemId TEXT, " +
                    "quantity INTEGER, " +
                    "price REAL, " +
                    "FOREIGN KEY(orderId) REFERENCES orders(orderId), " +
                    "FOREIGN KEY(itemId) REFERENCES menu_items(itemId))");

            // Insert some default menu items if table is empty
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('M1', 'Margherita Pizza', 'Fresh mozzarella and basil', 450.0, 'Pizza', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('M2', 'Coca Cola', 'Cold soft drink', 100.0, 'Beverage', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('M3', 'Burger', 'Veggie burger', 250.0, 'Snack', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");

            // Additional Drinks & Beverages
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('D1', 'Pepsi', 'Refreshing pepsi cola', 90.0, 'Beverage', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('D2', 'Fresh Lemonade', 'Home-made lemon juice', 120.0, 'Beverage', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('D3', 'Cappuccino', 'Rich Italian coffee', 180.0, 'Beverage', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('D4', 'Iced Tea', 'Peach flavored iced tea', 110.0, 'Beverage', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");

            // Additional Snacks
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('S1', 'French Fries', 'Crispy golden fries', 150.0, 'Snack', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('S2', 'Nachos', 'Cheesy nachos with salsa', 280.0, 'Snack', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('S3', 'Chicken Wings', 'Spicy buffalo wings', 350.0, 'Snack', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");

            // Fast Food
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('F1', 'Chicken Burger', 'Grilled chicken with cheese', 320.0, 'Fast Food', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('F2', 'Hot Dog', 'Classic beef hot dog', 220.0, 'Fast Food', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('F3', 'Club Sandwich', 'Triple decker sandwich', 290.0, 'Fast Food', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");

            // Dinner
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('DN1', 'Grilled Steak', 'Prime beef steak with mash', 1200.0, 'Dinner', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('DN2', 'Pasta Carbonara', 'Creamy pasta with bacon', 650.0, 'Dinner', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('DN3', 'Roast Chicken', 'Half roast chicken with herbs', 850.0, 'Dinner', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");

            // Special Food
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('SP1', 'Chef Special Thali', 'Full course traditional meal', 950.0, 'Special Food', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('SP2', 'Grilled Lobster', 'Fresh buttered lobster', 2500.0, 'Special Food', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");

            // Continental Food
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('C1', 'Beef Lasagna', 'Classic Italian lasagna', 750.0, 'Continental', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('C2', 'Ratatouille', 'French stewed vegetables', 550.0, 'Continental', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");
            stmt.execute("INSERT INTO menu_items (itemId, name, description, price, category, available) " +
                    "VALUES ('C3', 'Chicken Kiev', 'Breaded chicken with garlic butter', 800.0, 'Continental', 1) " +
                    "ON CONFLICT (itemId) DO NOTHING");

            // Insert default Admin user
            stmt.execute("INSERT INTO users (userId, name, email, phone, role, password, active) " +
                    "VALUES ('admin', 'System Admin', 'admin@foodorder.com', '9999999999', 'ADMIN', 'admin123', 1) " +
                    "ON CONFLICT (userId) DO NOTHING");

            System.out.println("✓ Database initialized");
        } catch (SQLException e) {
            System.err.println("✗ Database initialization failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            connect();
        }

        if (connection == null) {
            throw new IllegalStateException("Database connection not established. Check DB_URL, DB_USER, DB_PASSWORD and that PostgreSQL is running. See earlier logs for details.");
        }

        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database disconnected");
            }
        } catch (SQLException e) {
            System.err.println("✗ Disconnection failed: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getUsername() {
        return username;
    }

    public void setConnectionDetails(String connectionString, String username, String password) {
        this.connectionString = Objects.requireNonNull(connectionString, "connectionString");
        this.username = username == null ? "" : username;
        this.password = password == null ? "" : password;
    }
}
