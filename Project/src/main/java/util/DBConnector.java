package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    private Connection connection = null;

    public DBConnector() {
        // Tom konstruktør
    }

    public boolean connect(String url) {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Forbindelse til database oprettet.");
            return true;
        } catch (SQLException e) {
            System.out.println("Forbindelse til database fejlede: " + e.getMessage());
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database forbindelse lukket.");
            }
        } catch (SQLException e) {
            System.out.println("Fejl ved lukning af database: " + e.getMessage());
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Fejl ved forespørgsel: " + e.getMessage());
            return null;
        }
    }

    public int executeUpdate(String query) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Fejl ved opdatering: " + e.getMessage());
            return -1;
        }
    }

    public PreparedStatement prepareStatement(String query) {
        try {
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af prepared statement: " + e.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
