package com.foodordering.creational;

import com.foodordering.model.Customer;
import com.foodordering.model.RestaurantOwner;
import com.foodordering.model.Administrator;
import com.foodordering.model.User;

/**
 * FACTORY METHOD PATTERN - UserFactory
 * Creates different types of users based on role
 */
public class UserFactory {

    public static User createUser(String userType, String userId, String name, String email, String phone) {
        switch (userType.toUpperCase()) {
            case "CUSTOMER":
                return new Customer(userId, name, email, phone);
            case "RESTAURANT":
                return new RestaurantOwner(userId, name, email, phone, "Restaurant Name");
            case "ADMIN":
                return new Administrator(userId, name, email, phone, "STANDARD");
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }

    public static RestaurantOwner createRestaurant(String userId, String name, String email, String phone, String restaurantName) {
        return new RestaurantOwner(userId, name, email, phone, restaurantName);
    }

    public static Customer createCustomer(String userId, String name, String email, String phone) {
        return new Customer(userId, name, email, phone);
    }

    public static Administrator createAdmin(String userId, String name, String email, String phone, String level) {
        return new Administrator(userId, name, email, phone, level);
    }
}
