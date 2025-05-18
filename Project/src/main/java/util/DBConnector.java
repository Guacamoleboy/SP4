package util;

import App.BookingCard;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                    "hair_type_id TEXT DEFAULT '0'," +
                    "school_id TEXT DEFAULT '0'," +
                    "student_year TEXT DEFAULT 'N/A'," +
                    "profile_picture TEXT DEFAULT 'person1.png'," +
                    "language TEXT DEFAULT 'English'" +
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

    // Intet ID fordi den er autoincrement !! Vigtigt

    public boolean createUser(String username, String password, String email, String status, String banned, String role,
                              String profileHex, String bannerHex, String roleHex,
                              int hairTypeId, int schoolId, int studentYear,
                              String profilePicture, String language, String accepted, String darkmode) {

        String query = "INSERT INTO users (username, password, email, status, banned, role, profilehex, bannerhex, rolehex, " +
                "hair_type_id, school_id, student_year, profile_picture, language, accepted, darkmode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement createUserQuery = con.prepareStatement(query)) {
            createUserQuery.setString(1, username);
            createUserQuery.setString(2, password);
            createUserQuery.setString(3, email);
            createUserQuery.setString(4, status);
            createUserQuery.setString(5, banned);
            createUserQuery.setString(6, role);
            createUserQuery.setString(7, profileHex);
            createUserQuery.setString(8, bannerHex);
            createUserQuery.setString(9, roleHex);
            createUserQuery.setInt(10, hairTypeId);
            createUserQuery.setInt(11, schoolId);
            createUserQuery.setInt(12, studentYear);
            createUserQuery.setString(13, profilePicture);
            createUserQuery.setString(14, language);
            createUserQuery.setString(15, accepted);
            createUserQuery.setString(16, darkmode);

            int rows = createUserQuery.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("CreateUser failed. Check DBConnector ##DEBUG");
            e.printStackTrace();
            return false;
        }
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

    public int getUserID(String username) {
        int id = -1; // default value if not found
        String query = "SELECT id FROM users WHERE username = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id"); // parse the column to int
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

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
            userData.add(rs.getString("profilehex"));
            userData.add(rs.getString("bannerhex"));
            userData.add(rs.getString("rolehex"));
            userData.add(rs.getString("hair_type_id"));
            userData.add(rs.getString("school_id"));
            userData.add(rs.getString("student_year"));
            userData.add(rs.getString("profile_picture"));
            userData.add(rs.getString("language"));
            userData.add(rs.getString("accepted"));
            userData.add(rs.getString("darkmode"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userData;

    }

    // ____________________________________________________

    public boolean changePassword(String email, String password) {

        String query = "UPDATE users SET password = ? WHERE email = ?";

        try (PreparedStatement passwordChange = con.prepareStatement(query)) {

            passwordChange.setString(1, password);
            passwordChange.setString(2, email);

            int rowsAffected = passwordChange.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changePassword failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeDarkmode(String username, String mode) {

        String query = "UPDATE users SET darkmode = ? WHERE username = ?";

        try (PreparedStatement darkmodeChange = con.prepareStatement(query)) {

            darkmodeChange.setString(1, mode);
            darkmodeChange.setString(2, username);

            int rowsAffected = darkmodeChange.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changeDarkmode failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeBannerColor(String username, String color) {

        String query = "UPDATE users SET bannerhex = ? WHERE username = ?";

        try (PreparedStatement changeBanner = con.prepareStatement(query)) {

            changeBanner.setString(1, color);
            changeBanner.setString(2, username);
            int rowsAffected = changeBanner.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changeBannerColor failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeAccepted(String username, String accepted) {

        String query = "UPDATE users SET accepted = ? WHERE username = ?";

        try (PreparedStatement changeAccepted = con.prepareStatement(query)) {

            changeAccepted.setString(1, accepted);
            changeAccepted.setString(2, username);
            int rowsAffected = changeAccepted.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changeAccepted failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeUsername(String email, String newUsername) {

        String query = "UPDATE users SET username = ? WHERE email = ?";

        try (PreparedStatement changeAccepted = con.prepareStatement(query)) {

            changeAccepted.setString(1, newUsername);
            changeAccepted.setString(2, email);
            int rowsAffected = changeAccepted.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changeAccepted failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeProfilePicture(String username, String path) {

        String query = "UPDATE users SET profile_picture = ? WHERE username = ?";

        try (PreparedStatement changeProfilePicture = con.prepareStatement(query)) {

            changeProfilePicture.setString(1, path);
            changeProfilePicture.setString(2, username);
            int rowsAffected = changeProfilePicture.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changeProfilePicture failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeRoleColor(String username, String color) {

        String query = "UPDATE users SET rolehex = ? WHERE username = ?";

        try (PreparedStatement changeRoleHex = con.prepareStatement(query)) {

            changeRoleHex.setString(1, color);
            changeRoleHex.setString(2, username);

            int rowsAffected = changeRoleHex.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changeRoleColor failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeProfileColor(String username, String color) {

        String query = "UPDATE users SET profilehex = ? WHERE username = ?";

        try (PreparedStatement changeProfileHex = con.prepareStatement(query)) {

            changeProfileHex.setString(1, color);
            changeProfileHex.setString(2, username);

            int rowsAffected = changeProfileHex.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("changeProfileColor failed ##DEBUG");
            return false;

        }

    }

    // ____________________________________________________

    public boolean changeRole(String username, String role) {

        String query = "UPDATE users SET role = ? WHERE username = ?";

        try (PreparedStatement changeRole = con.prepareStatement(query)) {
            changeRole.setString(1, role);
            changeRole.setString(2, username);
            int rowsAffected = changeRole.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("ChangeRole DBConnector failed ##DEBUG");
            return false;
        }

    }

    // ____________________________________________________

    public boolean changeLanguage(String username, String language) {

        String query = "UPDATE users SET language = ? WHERE username = ?";

        try (PreparedStatement changeLanguage = con.prepareStatement(query)) {
            changeLanguage.setString(1, language);
            changeLanguage.setString(2, username);
            int rowsAffected = changeLanguage.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("ChangeLanguage failed ##DEBUG");
            return false;
        }

    }

    // ____________________________________________________

    public boolean deleteAccount(String username) {

        String query = "DELETE FROM users WHERE username = '" + username + "'";
        return executeUpdate(query);
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
    public String getHairTypeSummary(int id) {
        String sql = "SELECT texture, color, length, gender FROM hair_type_id WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("texture") + ", " +
                        rs.getString("color") + ", " +
                        rs.getString("length") + ", " +
                        rs.getString("gender");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
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

    public void createBooking(LocalDate date, String time, String address, int hairtype_id, boolean exam, int student_id) {
        String sql = "INSERT INTO bookings (date, time, address, hairtypeId, exam, student_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, date.toString());
            ps.setString(2, time);
            ps.setString(3, address);
            ps.setInt(4, hairtype_id);
            ps.setInt(5, exam ? 1 : 0);
            ps.setInt(6, student_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ____________________________________________________

    public List<BookingCard> getBookings(boolean isExam) {
        List<BookingCard> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE exam = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, isExam);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String time = rs.getString("time");
                String address = rs.getString("address");
                int hairtypeId = rs.getInt("hairtypeId");
                int studentId = rs.getInt("student_id");

                BookingCard booking = new BookingCard(date, time, address, hairtypeId, isExam, studentId);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // ____________________________________________________

    public List<BookingCard> getBookings(int studentId, boolean isExam) {
        List<BookingCard> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE student_id = ? AND exam = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setBoolean(2, isExam);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String time = rs.getString("time");
                String address = rs.getString("address");
                int hairtypeId = rs.getInt("hairtypeId");

                BookingCard booking = new BookingCard(date, time, address, hairtypeId, isExam, studentId);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // ____________________________________________________

    public String getEmail(String username) {

        String email = null;
        String query = "SELECT email FROM users WHERE username = ?";

        try (PreparedStatement emailGetter = con.prepareStatement(query)) {
            emailGetter.setString(1, username);

            ResultSet rs = emailGetter.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
            }

        } catch (SQLException e) {
            System.out.println("Email Getter failed ##DEBUG");
        }

        return email;

    }

    // ____________________________________________________

    public String getDarkmode(String username) {

        String darkmode = null;
        String query = "SELECT darkmode FROM users WHERE username = ?";

        try (PreparedStatement darkmodeGetter = con.prepareStatement(query)) {
            darkmodeGetter.setString(1, username);

            ResultSet rs = darkmodeGetter.executeQuery();

            if (rs.next()) {
                darkmode = rs.getString("darkmode");
            }

        } catch (SQLException e) {
            System.out.println("Darkmode Getter failed ##DEBUG");
        }

        return darkmode;

    }


} // DBConnector end