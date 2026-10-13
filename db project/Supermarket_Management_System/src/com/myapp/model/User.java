package com.myapp.model;

public class User {
    private String username;
    private String password;
    private Role role;
    private String branchId; // GLOBAL_ADMIN အတွက် "ALL" သို့မဟုတ် null ထားနိုင်သည်

    public User(String username, String password, Role role, String branchId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.branchId = branchId;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getBranchId() { return branchId; }
}