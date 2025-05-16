package util;

import App.HairType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                    "banned TEXT DEFAULT 'No'," +
                    "profilehex TEXT DEFAULT '#ADD8E6FF'," +
                    "bannerhex TEXT DEFAULT '#D3D3D3FF'," +
                    "rolehex TEXT DEFAULT '#d0e6f7'," +
                    "bannerurl TEXT" +
                    ")";

            String createMessagesTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender TEXT NOT NULL," +
                    "receiver TEXT NOT NULL," +
                    "content TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "dato DATE NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "place TEXT NOT NULL," +
                    "contactNumber TEXT NOT NULL," +
                    "bookingTimestamp DATETIME NOT NULL," +
                    "open TEXT NOT NULL CHECK(open IN ('yes', 'no'))," +
                    "notes TEXT DEFAULT 'Ingen noter..'" +
                    ")";
            stmt.execute(createBookingsTable);
            stmt.execute(createUsersTable);
            stmt.execute(createMessagesTable);

            // Check bannerurl, no?= add
            try {
                ResultSet rs = stmt.executeQuery("SELECT bannerurl FROM users LIMIT 1");
                rs.close();
            } catch (SQLException e) {

                stmt.execute("ALTER TABLE users ADD COLUMN bannerurl TEXT");
                System.out.println("Added bannerurl column to users table");
            }

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

    public boolean createUser(String username, String password, String email, String role) { //måske slettes
        String query = "INSERT INTO users (username, password, email, status, role) VALUES ('" +
                username + "', '" + password + "', '" + email + "', 'Offline', '" + role + "' )";
        return executeUpdate(query);
    }
    //For costumer
    public boolean createUser(String username, String password, String email, String role, int hairtype) {
        String query = "INSERT INTO users (username, password, email, status, role, hair_type_id) VALUES ('" +
                username + "', '" + password + "', '" + email + "', 'Offline', '" + role + "', '" + hairtype + "')";
        return executeUpdate(query);
    }
    //For student
    public boolean createUser(String username, String password, String email, String role, int schoolId, String student_year) {
        String query = "INSERT INTO users (username, password, email, status, role, school_id, student_year) VALUES ('" +
                username + "', '" + password + "', '" + email + "', 'Offline', '" + role + "', '" + schoolId + "', '" + student_year + "')";
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

        } catch (Exception e) {
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

            if (rs.next()) {
                userData.add(rs.getString("id"));
                userData.add(rs.getString("username"));
                userData.add(rs.getString("password"));
                userData.add(rs.getString("email"));
                userData.add(rs.getString("status"));
                userData.add(rs.getString("role"));
                userData.add(rs.getString("banned"));

                // add default values if the columns dontt exist
                String profileHex = rs.getString("profilehex");
                userData.add(profileHex != null ? profileHex : "#ADD8E6FF");

                String bannerHex = rs.getString("bannerhex");
                userData.add(bannerHex != null ? bannerHex : "#D3D3D3FF");

                String roleHex = rs.getString("rolehex");
                userData.add(roleHex != null ? roleHex : "#d0e6f7");

                // add banner URL if it exists
                try {
                    String bannerUrl = rs.getString("bannerurl");
                    if (bannerUrl != null) {
                        userData.add(bannerUrl);
                    }
                } catch (SQLException e) {
                    // column might not exist, ignore
                    System.out.println("bannerurl column not found: " + e.getMessage());
                }
            } else {
                //  add default values
                for (int i = 0; i < 10; i++) {
                    userData.add("");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting user data: " + e.getMessage());
            for (int i = 0; i < 10; i++) {
                userData.add("");
            }
        }

        return userData;
    }

    // ____________________________________________________

    public boolean changePassword(String username, String password){
        // TILFØJ HER

        return false;
    }

    // ____________________________________________________

    public boolean changeBannerColor(String color){
        // TILFØJ HER

        return false;
    }

    // ____________________________________________________

    public boolean changeRole(String username, String role){
        // TILFØJ HER

        if(role.equalsIgnoreCase("School")){
            // FUNKTION HER
        } else if (role.equalsIgnoreCase("Support")){
            // FUNKTION HER
        }

        return false;
    }

    // ____________________________________________________

    public boolean changeLanguage(String language){
        // TILFØJ HER

        return false;
    }

    // ____________________________________________________

    public boolean deleteAccount(String username){

        String query = "DELETE FROM users WHERE username = '" + username + "'";
        return executeUpdate(query);
    }

    // ____________________________________________________

    public boolean updateProfileColors(String username, String profileHex, String bannerHex, String roleHex, String bannerUrl) {
        try {
            // if bannerurl column exists
            boolean bannerUrlExists = true;
            try {
                Statement checkStmt = con.createStatement();
                ResultSet rs = checkStmt.executeQuery("SELECT bannerurl FROM users LIMIT 1");
                rs.close();
            } catch (SQLException e) {
                bannerUrlExists = false;

                Statement alterStmt = con.createStatement();
                alterStmt.execute("ALTER TABLE users ADD COLUMN bannerurl TEXT");

            }

            StringBuilder query = new StringBuilder("UPDATE users SET ");

            // update the color hex values
            query.append("profilehex = '").append(profileHex).append("', ")
                    .append("bannerhex = '").append(bannerHex).append("', ")
                    .append("rolehex = '").append(roleHex).append("'");

            // banner URL is provide
            if (bannerUrl != null && !bannerUrl.trim().isEmpty()) {
                query.append(", bannerurl = '").append(bannerUrl).append("'");
            }

            query.append(" WHERE username = '").append(username).append("'");

            return executeUpdate(query.toString());
        } catch (Exception e) {
            System.out.println("Error updating profile colors: " + e.getMessage());
            return false;
        }
    }

    public Map<String, Integer> getSchools() {
        Map<String, Integer> schoolMap = new HashMap<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id, name FROM schools");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                schoolMap.put(rs.getString("name"), rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schoolMap;
    }

    public int getOrCreateHairType(String texture, String color, String length, String gender) {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT id FROM hair_type_id WHERE texture=? AND color=? AND length=? AND gender=?");

            ps.setString(1, texture);
            ps.setString(2, color);
            ps.setString(3, length);
            ps.setString(4, gender);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                ps = con.prepareStatement(
                        "INSERT INTO hair_types (texture, color, length, gender) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, texture);
                ps.setString(2, color);
                ps.setString(3, length);
                ps.setString(4, gender);
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //If 0 - type not specified
    }

    //getter to the hairtype from db
    public HairType getHairTypeById(int id) {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM hair_types WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HairType(
                        rs.getInt("id"),
                        rs.getString("texture"),
                        rs.getString("color"),
                        rs.getString("length"),
                        rs.getString("gender")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Run this once from main-initialize to insert customer hairtype/gender in database.
    public void hairTypeInserter() {

        //edit here 
        String[] textures = {"Straight", "Wavy", "Curly", "Coily"};
        String[] colors = {"Blonde", "Black", "Brown", "Red", "Grey"};
        String[] lengths = {"Bald", "Buzz", "Short", "Medium", "Long", "Very Long", "Tied"};
        String[] genders = {"Male", "Female"};

        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO hair_type_id (texture, color, length, gender) VALUES (?, ?, ?, ?)"
            );

            for (String texture : textures) {
                for (String color : colors) {
                    for (String length : lengths) {
                        for (String gender : genders) {
                            ps.setString(1, texture);
                            ps.setString(2, color);
                            ps.setString(3, length);
                            ps.setString(4, gender);
                            ps.executeUpdate();
                        }
                    }
                }
            }

            System.out.println("All hair type combinations inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} // DBConnector end
