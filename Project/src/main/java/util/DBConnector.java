package util;

import java.sql.*;
import java.util.ArrayList;

public class DBConnector {

    // Attributes

    // DATATYPE //
    private boolean connected = false;

    // OBJECT //
    Connection con;

    // ____________________________________________________

    public boolean connect(String url) {

        try {
            con = DriverManager.getConnection(url);
            connected = true;
            initializeDatabase();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    // ____________________________________________________

    public boolean isConnected() {
        return connected;
    }

    // ____________________________________________________

    private void initializeDatabase() {

        try {
            Statement stmt = con.createStatement();
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "email TEXT," +
                    "status TEXT DEFAULT 'Offline'," +
                    "role TEXT NOT NULL," +
                    "banned TEXT DEFAULT 'No'" +
                    ")";

            String createMessagesTable = "CREATE TABLE IF NOT EXISTS messages (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender TEXT NOT NULL," +
                    "receiver TEXT NOT NULL," +
                    "content TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            stmt.execute(createUsersTable);
            stmt.execute(createMessagesTable);

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    // ____________________________________________________

    public ResultSet executeQuery(String query) {
        try {
            Statement stmt = con.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Query error: " + e.getMessage());
            return null;
        }
    }

    // ____________________________________________________

    public boolean executeUpdate(String query) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }

    // ____________________________________________________

    public boolean userExists(String username) {
        String query = "SELECT COUNT(*) as count FROM users WHERE username = '" + username + "'";

        try {
            ResultSet rs = executeQuery(query);
            if (rs != null && rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking user: " + e.getMessage());
        }
        return false;
    }

    // ____________________________________________________

    public boolean validateUser(String username, String password) {
        String query = "SELECT COUNT(*) as count FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        try {
            ResultSet rs = executeQuery(query);
            if (rs != null && rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error validating user: " + e.getMessage());
        }
        return false;
    }

    // ____________________________________________________

    public boolean createUser(String username, String password, String email, String role) {
        String query = "INSERT INTO users (username, password, email, status, role) VALUES ('" +
                username + "', '" + password + "', '" + email + "', 'Offline', '" + role + "' )";
        return executeUpdate(query);
    }

    // ____________________________________________________

    public boolean updateUserStatus(String username, String status) {
        String query = "UPDATE users SET status = '" + status + "' WHERE username = '" + username + "'";
        return executeUpdate(query);
    }

    // ____________________________________________________

    public boolean saveMessage(String sender, String receiver, String content) {
        String query = "INSERT INTO messages (sender, receiver, content) VALUES ('" +
                sender + "', '" + receiver + "', '" + content + "')";
        return executeUpdate(query);
    }

    // ____________________________________________________

    public ResultSet getMessages(String user1, String user2) {
        String query = "SELECT * FROM messages WHERE " +
                "(sender = '" + user1 + "' AND receiver = '" + user2 + "') OR " +
                "(sender = '" + user2 + "' AND receiver = '" + user1 + "') " +
                "ORDER BY timestamp ASC";
        return executeQuery(query);
    }

    // ____________________________________________________

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                connected = false;
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    // ____________________________________________________

    public String getRole(String username) {

        String role = "";
        String query = "SELECT role FROM users WHERE username = '" + username + "'";

        try {

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            role = rs.getString("role");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return role;

    }

    // ____________________________________________________

    public boolean createNewDatabase(String url) {

        try {
            String dbFile = url.replace("jdbc:sqlite:", "");

            java.io.File file = new java.io.File(dbFile);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created a database at:" + dbFile);

            }
            con = java.sql.DriverManager.getConnection(url);
            connected = true;
            initializeDatabase();
            return true;

        } catch (Exception e){
            System.out.println("fejl tissemand");
            return false;
        }

    }

    // ____________________________________________________

    public ArrayList<String> getUserData(String username) {
        ArrayList<String> userData = new ArrayList<String>();
        String query = "SELECT * FROM users WHERE username = '" + username + "'";

        try {

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            userData.add(rs.getString("id"));
            userData.add(rs.getString("username"));
            userData.add(rs.getString("password"));
            userData.add(rs.getString("email"));
            userData.add(rs.getString("status"));
            userData.add(rs.getString("role"));
            userData.add(rs.getString("banned"));

            /*
            userData.add(rs.getString("profilePicture"));
            userData.add(rs.getString("profileBanner"));
            */

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userData;

    }

} // DBConnector end
