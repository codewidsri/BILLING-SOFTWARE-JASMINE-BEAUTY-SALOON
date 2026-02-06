package com.billingapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String URL = "jdbc:sqlite:billing.db";
    // private static volatile Connection connection;

    private DataBaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        // try {
        //     if (connection == null || connection.isClosed()) {
        //         synchronized (DataBaseConnection.class) {
        //             if (connection == null || connection.isClosed()) {
        //                 connection = DriverManager.getConnection(URL);
        //                 System.out.println("Database connected");
        //             }
        //         }
        //     }
        // } catch (SQLException e) {
        //     System.out.println(e.getMessage());
        // }
        // return connection;
        return DriverManager.getConnection(URL);
    }

}