package com.foodordering.creational;

/**
 * SINGLETON PATTERN - DatabaseManager
 * Ensures only one instance of database connection throughout the application
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static final Object lock = new Object();
    private String connectionString;
    private boolean isConnected;

    private DatabaseManager() {
        this.connectionString = "jdbc:mysql://localhost:3306/food_ordering";
        this.isConnected = false;
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

    public void connect() {
        isConnected = true;
        System.out.println("✓ Database connected: " + connectionString);
    }

    public void disconnect() {
        isConnected = false;
        System.out.println("✓ Database disconnected");
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getConnectionString() {
        return connectionString;
    }
}
