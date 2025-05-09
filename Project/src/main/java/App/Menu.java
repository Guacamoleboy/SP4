package App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.DBConnector;
import App.User;

public class Main extends Application {
    private static DBConnector db;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/App/hello-view.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ElevTiden");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Initialize database connection
        db = new DBConnector();
        if (db.connect("jdbc:sqlite:ElevTiden.sqlite")) {
            System.out.println("Successfully connected to the database");

            // Create tables if they don't exist
            createTables();

            // Create a sample user
            createUser("john_doe", "password123", "John", "Doe", "john.doe@example.com");

            // Display all users
            displayAllUsers();
        } else {
            System.out.println("Failed to connect to the database");
        }

        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void stop() {
        if (db != null) {
            db.closeConnection();
        }
    }


    private static void createTables() {
        String createUserTableSQL =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL UNIQUE," +
                        "password TEXT NOT NULL," +
                        "first_name TEXT," +
                        "last_name TEXT," +
                        "email TEXT UNIQUE" +
                        ");";

        int result = db.executeUpdate(createUserTableSQL);
        if (result != -1) {
            System.out.println("User table created or already exists");
        }
    }


    private static void createUser(String username, String password, String firstName, String lastName, String email) {
        try {
            // findes ?
            String checkUserSQL = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = db.prepareStatement(checkUserSQL);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("User " + username + " already exists");
                return;
            }

            // yes opret
            String insertUserSQL =
                    "INSERT INTO users (username, password, first_name, last_name, email) " +
                            "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = db.prepareStatement(insertUserSQL);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, firstName);
            insertStmt.setString(4, lastName);
            insertStmt.setString(5, email);

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + username + " created successfully");

                // Create User object
                User user = new User();
                user.setUsername(username);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                System.out.println("User object created: " + user);
            } else {
                System.out.println("Failed to create user");
            }

        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private static void displayAllUsers() {
        String query = "SELECT * FROM users";
        ResultSet rs = db.executeQuery(query);

        try {
            System.out.println("\n--- All Users ---");
            while (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");

                System.out.println("ID: " + id +
                        ", Username: " + username +
                        ", Name: " + firstName + " " + lastName +
                        ", Email: " + email);
            }
            System.out.println("----------------\n");
        } catch (SQLException e) {
            System.out.println("Error displaying users: " + e.getMessage());
        }
    }
}
