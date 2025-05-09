package App;

import util.*;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessData {

    // Attributes
    private Login login = new Login(900, 600);
    private ArrayList<User> user;
    private DBConnector db;
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    // ____________________________________________________

    public ProcessData() {
        this.user = new ArrayList<>();
        this.db = new DBConnector();
        initializeDatabase();
        loadUserData();
    }

    // ____________________________________________________

    private void initializeDatabase() {
        if (db.connect(DB_URL)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT, " +
                    "email TEXT, " +
                    "status TEXT, " +
                    "banned TEXT)";
            db.executeUpdate(createTableSQL);
        }
    }

    // ____________________________________________________

    public void saveData() {
        if (!db.isConnected()) {
            db.connect(DB_URL);
        }

        for (User u : user) {
            String checkUserSQL = "SELECT * FROM users WHERE username = ?";
            try {
                PreparedStatement checkStmt = db.prepareStatement(checkUserSQL);
                checkStmt.setString(1, u.getUsername());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Update  user
                    String updateSQL = "UPDATE users SET password = ?, email = ?, status = ?, banned = ? WHERE username = ?";
                    PreparedStatement updateStmt = db.prepareStatement(updateSQL);
                    updateStmt.setString(1, u.getPassword());
                    updateStmt.setString(2, u.getEmail());
                    updateStmt.setString(3, u.getStatus());
                    updateStmt.setString(4, u.getBanned());
                    updateStmt.setString(5, u.getUsername());
                    updateStmt.executeUpdate();
                } else {
                    // inset new user
                    String insertSQL = "INSERT INTO users (username, password, email, status, banned) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = db.prepareStatement(insertSQL);
                    insertStmt.setString(1, u.getUsername());
                    insertStmt.setString(2, u.getPassword());
                    insertStmt.setString(3, u.getEmail());
                    insertStmt.setString(4, u.getStatus());
                    insertStmt.setString(5, u.getBanned());
                    insertStmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println("Error saving user data: " + e.getMessage());
            }
        }
    }

    // ____________________________________________________

    public void setStatus(String username, String status) {
        for (User u : user) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                u.setStatus(status);
                break;
            }
        }
        saveData();
    }

    // ____________________________________________________

    public void loadUserData() {
        if (!db.isConnected()) {
            db.connect(DB_URL);
        }

        user.clear();
        String query = "SELECT * FROM users";
        ResultSet rs = db.executeQuery(query);

        try {
            while (rs != null && rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String status = rs.getString("status");
                String banned = rs.getString("banned");

                createUser(username, password, email, status, banned);
            }
        } catch (SQLException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    // ____________________________________________________

    public void createUser(String username, String password, String email, String status, String banned) {
        User u = new User(username, password, email, status, banned);
        user.add(u);
    }

    // ____________________________________________________

    public ArrayList<User> getUsers() {
        return user;
    }
}
