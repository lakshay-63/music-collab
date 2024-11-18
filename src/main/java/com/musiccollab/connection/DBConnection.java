package com.musiccollab.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/music_collab";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "12345678"; // Replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure MySQL JDBC Driver is loaded
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
