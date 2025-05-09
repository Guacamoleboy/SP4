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


public class Main extends Application {
    private static DBConnector db;
    private static ProcessData processData;

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
        processData = new ProcessData();

        db = new DBConnector();
        if (db.connect("jdbc:sqlite:ElevTiden.sqlite")) {
            System.out.println("Forbundet til databasen");

            // Opret tabeller hvis de ikke findes
            createTables();

            // sync process til db
            syncUsersToDatabase();

            // Vis alle brugere
            displayAllUsers();
        } else {
            System.out.println("Kunne ikke forbinde til databasen");
        }

        // Start jonas gui
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
                        "email TEXT UNIQUE," +
                        "status TEXT," +
                        "banned TEXT" +
                        ");";

        int result = db.executeUpdate(createUserTableSQL);
        if (result != -1) {
            System.out.println("Brugertabel oprettet eller findes allerede");
        }
    }

    private static void syncUsersToDatabase() {
        for (User user : processData.getUsers()) {
            try {
                // tjek li om dude findes
                String checkUserSQL = "SELECT * FROM users WHERE username = ?";
                PreparedStatement checkStmt = db.prepareStatement(checkUserSQL);
                checkStmt.setString(1, user.getUsername());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // update ved users
                    String updateUserSQL =
                            "UPDATE users SET password = ?, email = ?, status = ?, banned = ? " +
                                    "WHERE username = ?";

                    PreparedStatement updateStmt = db.prepareStatement(updateUserSQL);
                    updateStmt.setString(1, user.getPassword());
                    updateStmt.setString(2, user.getEmail());
                    updateStmt.setString(3, user.getStatus());
                    updateStmt.setString(4, user.getBanned());
                    updateStmt.setString(5, user.getUsername());

                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Bruger " + user.getUsername() + " opdateret i databasen");
                    }
                } else {
                    // insert af bruger
                    String insertUserSQL =
                            "INSERT INTO users (username, password, email, status, banned) " +
                                    "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement insertStmt = db.prepareStatement(insertUserSQL);
                    insertStmt.setString(1, user.getUsername());
                    insertStmt.setString(2, user.getPassword());
                    insertStmt.setString(3, user.getEmail());
                    insertStmt.setString(4, user.getStatus());
                    insertStmt.setString(5, user.getBanned());

                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Bruger " + user.getUsername() + " tilføjet til databasen");
                    }
                }
            } catch (SQLException e) {
                System.out.println("lort sket ved sync: " + e.getMessage());
            }
        }
    }

    private static void displayAllUsers() {
        String query = "SELECT * FROM users";
        ResultSet rs = db.executeQuery(query);

        try {
            System.out.println("\n--- Alle Brugere i Database ---");
            while (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String status = rs.getString("status");
                String banned = rs.getString("banned");

                System.out.println("ID: " + id +
                        ", Brugernavn: " + username +
                        ", Email: " + email +
                        ", Status: " + status +
                        ", Banned: " + banned);
            }
            System.out.println("----------------\n");

            // Vis også brugere fra vores processdata
            System.out.println("--- Alle Brugere i ProcessData ---");
            for (User user : processData.getUsers()) {
                System.out.println("Brugernavn: " + user.getUsername() +
                        ", Email: " + user.getEmail() +
                        ", Status: " + user.getStatus() +
                        ", Banned: " + user.getBanned());
            }
            System.out.println("----------------\n");
        } catch (SQLException e) {
            System.out.println("Fejl ved visning af brugere: " + e.getMessage());
        }
    }
}
