package com.myapp.session;

public class UserSession {
    private static int id;
    private static String username;
    private static String role;
    private static String branchId;

    public static void createSession(int userId, String uname, String userRole, String branch) {
        id = userId;
        username = uname;
        role = userRole;
        branchId = branch;
    }

    public static int getId() {
        return id;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static String getBranchId() {
        return branchId;
    }

    public static String getCurrentBranch() {
        return branchId;
    }

    public static void logout() {
        id = 0;
        username = null;
        role = null;
        branchId = null;
    }
}