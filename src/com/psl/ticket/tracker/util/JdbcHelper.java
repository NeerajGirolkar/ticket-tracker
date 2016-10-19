package com.psl.ticket.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcHelper {
    private static JdbcHelper instance = new JdbcHelper();
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/tickets-tracker-db";
    private final String USERNAME = "root";
    private final String PASSWORD = "admin";

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error: Not able to connect to MySQL Database.");
        }
        return connection;
    }   
     
    public static Connection getConnection() {
        return instance.createConnection();
    }
    
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            	System.out.println("Error: Not able to close connection.");
            }
        }
    }
 
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
            	System.out.println("Error: Not able to close statement.");
            }
        }
    }
 
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
            	System.out.println("Error: Not able to close ResultSet.");
            }
        }
    }
}
