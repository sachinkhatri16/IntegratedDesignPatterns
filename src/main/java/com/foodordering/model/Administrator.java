package com.foodordering.model;

/**
 * Administrator class for system management
 */
public class Administrator extends User {
    private String adminLevel;
    private boolean canApproveRestaurants;
    private boolean canManageUsers;

    public Administrator(String userId, String name, String email, String phone, String adminLevel) {
        super(userId, name, email, phone, UserRole.ADMIN);
        this.adminLevel = adminLevel;
        this.canApproveRestaurants = adminLevel.equals("SUPER");
        this.canManageUsers = true;
    }

    @Override
    public void displayRole() {
        System.out.println("I am an Administrator");
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public boolean canApproveRestaurants() {
        return canApproveRestaurants;
    }

    public boolean canManageUsers() {
        return canManageUsers;
    }
}
