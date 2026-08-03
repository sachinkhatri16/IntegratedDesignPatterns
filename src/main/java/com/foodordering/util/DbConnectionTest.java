package com.foodordering.util;

import com.foodordering.creational.DatabaseManager;
import java.sql.Connection;

/**
 * Simple utility to test if the database connection works.
 * Run this to verify PostgreSQL is running and configured correctly.
 */
public class DbConnectionTest {
    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("DATABASE CONNECTION TEST");
        System.out.println("========================================\n");

        // Print current settings
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/food_ordering");
        String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "");

        System.out.println("Current Settings:");
        System.out.println("  DB_URL: " + dbUrl);
        System.out.println("  DB_USER: " + dbUser);
        System.out.println("  DB_PASSWORD: " + (dbPassword == null || dbPassword.isBlank() ? "(empty)" : "(provided, masked)"));
        System.out.println();

        // Attempt connection
        System.out.println("Attempting to connect...");
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ SUCCESS! Database connection established.");
                System.out.println("✓ Tables created and initialized.");
                dbManager.disconnect();
                System.out.println("\nDatabase is ready to use!");
            } else {
                System.out.println("✗ FAILED: Connection is null or closed.");
                System.out.println("\nTroubleshooting:");
                System.out.println("  1. Ensure PostgreSQL service is running (check Windows Services)");
                System.out.println("  2. Verify the database 'food_ordering' exists");
                System.out.println("  3. Verify the user/password are correct");
                System.out.println("  4. Run PowerShell as admin and execute:");
                System.out.println("     psql -U postgres -h localhost -p 5432 -c \"CREATE DATABASE food_ordering;\"");
                System.out.println("     psql -U postgres -h localhost -p 5432 -c \"ALTER USER postgres WITH PASSWORD '1928374650@Asd';\"");
            }
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            System.out.println("\nTroubleshooting:");
            System.out.println("  1. Ensure PostgreSQL service is running");
            System.out.println("  2. Verify the database 'food_ordering' exists");
            System.out.println("  3. Check DB_URL, DB_USER, DB_PASSWORD environment variables");
            System.out.println("\nTo create the database, run in PowerShell:");
            System.out.println("  psql -U postgres -h localhost -p 5432 -c \"CREATE DATABASE food_ordering;\"");
            System.out.println("  psql -U postgres -h localhost -p 5432 -c \"ALTER USER postgres WITH PASSWORD '1928374650@Asd';\"");
            e.printStackTrace();
        }

        System.out.println("\n========================================\n");
    }
}

