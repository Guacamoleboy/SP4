/*

    SP4 - ICE School Project

    Author(s):
    Ebou, Andreas, Carl-Emil & Jonas

    Comments
    ________

    N/A


*/

// Packages
package App;

// Imports
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.DBConnector;

import static App.UpdateChecker.getCurrentVersion;

public class Main extends Application { // Client class

    // Attributes
    private Login login = new Login(600, 600);
    private StartInfo startinfo = new StartInfo(300, 600);
    private StartBorder startborder = new StartBorder(3);
    private static DBConnector db;

    //Colors for the whole program
    public static String backgroundColor = "#AAAAAAFF";
    public static String barColor = "orange";

    // ____________________________________________________

    @Override
    public void start(Stage app) {
        String version = getCurrentVersion();
        app.setTitle("SP4 Version "+version);

        // Side by side layout JavaFX (left -> Right) // HBox
        HBox mainScene = new HBox(startinfo, startborder, login); // Start Scene

        Scene scene = new Scene(mainScene, 900, 600);
        app.setResizable(false); // Doesn't allow changeing of width / height
        app.setScene(scene);

        // Logo for application. It's under resources.
        app.getIcons().add(new Image(getClass().getResource("/assets/logo/logo-128.png").toExternalForm()));

        closeHandle(app);

        app.show();
    }

    // _______________________MAIN_________________________

    public static void main(String[] args) {
        // Start database forbindelse
        db = new DBConnector();
        if (db.connect("jdbc:sqlite:ElevTiden.sqlite")) {
            System.out.println("Forbundet til databasen");

            createTables();

            createDefaultUserIfNeeded();
            displayAllUsers();
        } else {
            System.out.println("Kunne ikke forbinde til databasen");
        }

        // Start JavaFX applikation
        launch();
    }

    // ____________________________________________________


    /*

        If a user closes the app then it should change their status to "Offline" again.
        This code does exactly that. Only for the logged in user. Else all would become "Offline" if
        anyone closes the app.

    */

    public void closeHandle(Stage stage){

        stage.setOnCloseRequest(e -> {
            String username = login.getUsername();

            // Opdater brugerens status til "Offline" i databasen. Pretty useless men ja...
            if (username != null && !username.isEmpty()) {
                updateUserStatus(username, "Offline");
            }

            // Luk database forbindelsen når programmet afsluttes (vigtigt)
            if (db != null) {
                db.closeConnection();
            }

            System.exit(0);
        });
    }

    // ____________________________________________________

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

    private static void createDefaultUserIfNeeded() {
        try {
            // Tjek om der er brugere i databasen
            String countSQL = "SELECT COUNT(*) as count FROM users";
            ResultSet countRs = db.executeQuery(countSQL);

            if (countRs != null && countRs.next() && countRs.getInt("count") == 0) {
                // Opret en standard bruger hvis databasen er tom
                createUser("admin", "password", "Admin", "User", "admin@elevtiden.dk", "Online", "false");
                createUser("user1", "password", "Test", "Bruger", "user1@nextfrisør.dk", "Offline", "false");
                System.out.println("Standard brugere oprettet");
            }
        } catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af standard brugere: " + e.getMessage());
        }
    }

    private static void createUser(String username, String password, String firstName, String lastName, String email, String status, String banned) {
        try {
            // Tjek om brugeren allerede findes
            String checkUserSQL = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = db.prepareStatement(checkUserSQL);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Bruger " + username + " findes allerede");
                return;
            }

            // Opret ny bruger
            String insertUserSQL =
                    "INSERT INTO users (username, password, first_name, last_name, email, status, banned) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = db.prepareStatement(insertUserSQL);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, firstName);
            insertStmt.setString(4, lastName);
            insertStmt.setString(5, email);
            insertStmt.setString(6, status);
            insertStmt.setString(7, banned);

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bruger " + username + " oprettet");

                // Opret User objekt
                User user = new User(username, password, firstName, lastName, email, status, banned);
                System.out.println("Bruger objekt oprettet: " + user);
            } else {
                System.out.println("Kunne ikke oprette bruger");
            }

        } catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af bruger: " + e.getMessage());
        }
    }

    private static void updateUserStatus(String username, String status) {
        try {
            String updateSQL = "UPDATE users SET status = ? WHERE username = ?";
            PreparedStatement updateStmt = db.prepareStatement(updateSQL);
            updateStmt.setString(1, status);
            updateStmt.setString(2, username);

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status for " + username + " opdateret til " + status);
            } else {
                System.out.println("Kunne ikke opdatere status for " + username);
            }
        } catch (SQLException e) {
            System.out.println("Fejl ved opdatering af bruger status: " + e.getMessage());
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
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String status = rs.getString("status");
                String banned = rs.getString("banned");

                System.out.println("ID: " + id +
                        ", Brugernavn: " + username +
                        ", Navn: " + (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "") +
                        ", Email: " + email +
                        ", Status: " + status +
                        ", Banned: " + banned);
            }
            System.out.println("----------------\n");
        } catch (SQLException e) {
            System.out.println("Fejl ved visning af brugere: " + e.getMessage());
        }
    }

    // load en bruger fra databasen baseret på brugernavn
    public static User getUserByUsername(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String status = rs.getString("status");
                String banned = rs.getString("banned");

                return new User(id, username, password, firstName, lastName, email, status, banned);
            }
        } catch (SQLException e) {
            System.out.println("Fejl ved load af bruger: " + e.getMessage());
        }

        return null;
    }

    // update en brugers information i databasen
    public static boolean updateUser(User user) {
        try {
            String updateSQL =
                    "UPDATE users SET password = ?, first_name = ?, last_name = ?, " +
                            "email = ?, status = ?, banned = ? WHERE username = ?";

            PreparedStatement updateStmt = db.prepareStatement(updateSQL);
            updateStmt.setString(1, user.getPassword());
            updateStmt.setString(2, user.getFirstName());
            updateStmt.setString(3, user.getLastName());
            updateStmt.setString(4, user.getEmail());
            updateStmt.setString(5, user.getStatus());
            updateStmt.setString(6, user.getBanned());
            updateStmt.setString(7, user.getUsername());

            int rowsAffected = updateStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Fejl ved opdatering af bruger: " + e.getMessage());
            return false;
        }
    }

} // Class end
