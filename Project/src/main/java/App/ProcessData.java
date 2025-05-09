package App;

import util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProcessData {

    // Attributes
    private Login login = new Login(900, 600);
    private DBConnector dbConnector = new DBConnector();
    private ArrayList <User> user;

    // ____________________________________________________

    public ProcessData(){
        this.user = new ArrayList<>();
        // forbinder til database
        boolean connected = dbConnector.connect("jdbc:sqlite:ElevTiden.sqlite");
        if (connected) {
            // laver tabel hvis den ikke findes
            createTableIfNotExists();
            loadUserData();
        } else {
            System.out.println("Failed to connect to database");
        }
    }

    // ____________________________________________________

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "email TEXT," +
                "status TEXT," +
                "banned TEXT)";

        dbConnector.executeUpdate(createTableSQL);
    }

    // ____________________________________________________

    public void saveData(){
        // ikke nødvendig, database opdateres direkte (det jeg mener er at vi gemmer ting live,
        // modsat måden vi før gjorde med at gemme i en liste

    }

    // ____________________________________________________

    public void setStatus(String username, String status){
        String updateSQL = "UPDATE users SET status = ? WHERE username = ?";

        try {
            PreparedStatement pstmt = dbConnector.prepareStatement(updateSQL);
            pstmt.setString(1, status);
            pstmt.setString(2, username);
            pstmt.executeUpdate();

            // opdaterer også den "local" / eksisterende liste
            for (User u : user){
                if(u.getUsername().equalsIgnoreCase(username)){
                    u.setStatus(status);
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating user status: " + e.getMessage());
        }
    }

    // ____________________________________________________

    public void loadUserData(){
        String query = "SELECT * FROM users";

        try {
            ResultSet rs = dbConnector.executeQuery(query);

            while (rs != null && rs.next()) {
                String valuesUsername = rs.getString("username");
                String valuesPassword = rs.getString("password");
                String valuesEmail = rs.getString("email");
                String valuesStatus = rs.getString("status");
                String valuesBanned = rs.getString("banned");

                createUser(valuesUsername, valuesPassword, valuesEmail, valuesStatus, valuesBanned);
            }

            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    // ____________________________________________________

    public void createUser(String username, String password, String email, String status, String banned){
        // opretter bruger i session / instansiere i programmet
        User u = new User(username, password, email, status, banned);
        user.add(u);

        // tjekker om brugeren allerede findes
        try {
            ResultSet rs = dbConnector.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            boolean userExists = rs != null && rs.next();
            rs.close();

            if (!userExists) {
                // indsætter bruger i databasen
                String insertSQL = "INSERT INTO users (username, password, email, status, banned) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = dbConnector.prepareStatement(insertSQL);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.setString(4, status);
                pstmt.setString(5, banned);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error creating user in database: " + e.getMessage());
        }
    }

    // ____________________________________________________

    public ArrayList <User> getUsers(){
        return user;
    }

    // ____________________________________________________

    // lukker forbindelsen når objektet smides ud
    @Override
    protected void finalize() throws Throwable {
        try {
            dbConnector.closeConnection();
        } finally {
            super.finalize();
        }
    }
}
