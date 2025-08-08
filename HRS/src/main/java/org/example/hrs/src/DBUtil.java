package org.example.hrs.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage database connections for the Hotel Reservation System.
 * Uses JDBC to connect to a MySQL database.
 */
public class DBUtil {

    // JDBC connection URL to the MySQL database
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_reservation_system";

    // Database user credentials
    private static final String USER = "root";
    private static final String PASSWORD = "habib@123";

    /**
     * Establishes and returns a connection to the MySQL database.
     *
     * @return A {@link Connection} object to interact with the database.
     * @throws SQLException If the connection fails (e.g., wrong credentials, server not running).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


