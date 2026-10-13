package com.myapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/supermarket_db";
    private static final String USER = "root"; 
    
    // MySQL Command Line Client ဝင်တုန်းက ရိုက်ခဲ့တဲ့ Password ထည့်ပါ (Password မရှိပါက "" ဟုထားပါ)
    private static final String PASSWORD = "root"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}