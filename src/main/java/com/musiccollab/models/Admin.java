package com.musiccollab.models;

public class Admin extends User {

    // Example: admin's ability to manage users and content
    private Boolean canManageUsers;

    public Boolean getCanManageUsers() {
        return canManageUsers;
    }

    public void setCanManageUsers(Boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }
}
