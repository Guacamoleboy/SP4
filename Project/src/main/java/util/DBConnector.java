package util;

import App.AcceptedBooking;
import App.BookingCard;
import App.Tip;
import App.Request;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.sql.DriverManager.getConnection;

public class DBConnector {

    // Attributes

    // DATATYPE //
    private boolean connected = false;

    // OBJECT //
    Connection con;

    // ____________________________________________________

    public boolean connect(String url) {

        try {
            con = getConnection(url);
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
                    "language TEXT DEFAULT 'English'," +
                    "accepted TEXT DEFAULT NULL," +
                    "darkmode BOOLEAN DEFAULT NULL," +
                    "lastonline TEXT DEFAULT 'NOT SEEN'," +
                    "city TEXT," +
                    "abtmeheader TEXT DEFAULT 'Hello...'," +
                    "abtmedesc TEXT DEFAULT 'Welcome to my profile...'," +
                    "abtmefunfacts TEXT DEFAULT 'No numbers before 1000 contains the letter A.'," +
                    "phone INTEGER DEFAULT '00000000'," +
                    "rating DOUBLE DEFAULT '0.0'," +
                    "social TEXT," +
                    "contactheader TEXT DEFAULT 'I am glad you are here...'," +
                    "contactdesc TEXT DEFAULT 'Welcome my friend. You can get in contact with me here! Hope to hear from ya!'," +
                    "student_id INTEGER" +
                    ")";

            String createMessagesTable = "CREATE TABLE IF NOT EXISTS messages (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender TEXT NOT NULL," +
                    "receiver TEXT NOT NULL," +
                    "content TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            String createRequestTable = "CREATE TABLE IF NOT EXISTS request (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender TEXT NOT NULL," +
                    "receiver TEXT NOT NULL," +
                    "comment TEXT" +
                    ")";

            String createTipTable = "CREATE TABLE IF NOT EXISTS tip (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender TEXT NOT NULL," +
                    "receiver TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "comment TEXT" +
                    ")";

            String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date TEXT NOT NULL," +
                    "time TEXT DEFAULT CURRENT_TIMESTAMP," +
                    "address TEXT NOT NULL," +
                    "hairtypeId INTEGER NOT NULL," +
                    "exam INTEGER," +
                    "student_id INTEGER," +
                    "paid TEXT," +
                    "accepted TEXT," +
                    "customer TEXT," +
                    "FOREIGN KEY (hairtypeId) REFERENCES hair_type_id(id)," +
                    "FOREIGN KEY (student_id) REFERENCES users(id)" +
                    ")";


            String createHairTypeTable = "CREATE TABLE IF NOT EXISTS hair_type_id (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "texture TEXT DEFAULT NULL," +
                    "color TEXT DEFAULT NULL," +
                    "length TEXT DEFAULT NULL," +
                    "gender TEXT DEFAULT NULL" +
                    ")";

            String createSchoolTable = "CREATE TABLE IF NOT EXISTS schools (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL" +
                    ")";


            String createInterests = "CREATE TABLE IF NOT EXISTS interest (" +
                    "id INTEGER NOT NULL," +
                    "name TEXT NOT NULL," +
                    "PRIMARY KEY (id, name)" +
                    ")";


            String insertSchools = "INSERT OR IGNORE INTO schools (id, name) VALUES " +
                    "(1, 'EUC SJÆLLAND NÆSTVED')," +
                    "(2, 'EUC SYD STEGHOLT')," +
                    "(3, 'HANSENBERG')," +
                    "(4, 'HERNINGSHOLM ERHVERVSSKOLE')," +
                    "(5, 'NEXT UDDANNELSE KØBENHAVN')," +
                    "(6, 'EUD: RYBNERS SPANGSBJERG')," +
                    "(7, 'SYDDANSK ERHVERVSSKOLE ODENSE')," +
                    "(8, 'TECHCOLLEGE, STYLE & WELLNESS')," +
                    "(9, 'TRADIUM, TEKNISKE ERHVERVSUDDANNELSER VA')," +
                    "(10, 'AARHUS TECH')," +
                    "(11, 'KØBENHAVNS FRISØRSKOLE')," +
                    "(12, 'BEAUTY AND STYLE')";

            stmt.executeUpdate(createUsersTable);
            stmt.executeUpdate(createMessagesTable);
            stmt.executeUpdate(createRequestTable);
            stmt.executeUpdate(createTipTable);
            stmt.executeUpdate(createBookingsTable);
            stmt.executeUpdate(createHairTypeTable);
            stmt.executeUpdate(createSchoolTable);
            stmt.executeUpdate(insertSchools);
            stmt.executeUpdate(createInterests);

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

    public boolean isUserBanned(String username) {
        String query = "SELECT banned FROM users WHERE username = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs != null && rs.next()) {
                String bannedValue = rs.getString("banned");
                return bannedValue != null && bannedValue.equalsIgnoreCase("yes");
            }
        } catch (SQLException e) {
            System.out.println("Error checking banned status: " + e.getMessage());
        }
        return false;
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

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) as count FROM users WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs != null && rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking email: " + e.getMessage());
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
                              String profilePicture, String language, String accepted, String darkmode, String lastonline,
                              String city, String abtmeheader, String abtmedesc, String abtmefunfacts, int phone, double rating,
                              String social, String contactheader, String contactdesc) {

        String query = "INSERT INTO users (username, password, email, status, banned, role, profilehex, bannerhex, rolehex, " +
                "hair_type_id, school_id, student_year, profile_picture, language, accepted, darkmode, lastonline, " +
                "city, abtmeheader, abtmedesc, abtmefunfacts, phone, rating, social, contactheader, contactdesc) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            createUserQuery.setString(17, lastonline);
            createUserQuery.setString(18, city);
            createUserQuery.setString(19, abtmeheader);
            createUserQuery.setString(20, abtmedesc);
            createUserQuery.setString(21, abtmefunfacts);
            createUserQuery.setInt(22, phone);
            createUserQuery.setDouble(23, rating);
            createUserQuery.setString(24, social);
            createUserQuery.setString(25, contactheader);
            createUserQuery.setString(26, contactdesc);
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
        System.out.println("Inserts?"+ content);
        String query = "INSERT INTO messages (sender, receiver, content) VALUES ('" +
                sender + "', '" + receiver + "', '" + content + "')";
        return executeUpdate(query);
    }

    // ____________________________________________________

    public ArrayList<String> loadYourMessages(String user) {
        ArrayList<String> persons = new ArrayList<>();
        String query = "SELECT DISTINCT CASE " +
                "WHEN sender = ? THEN receiver " +
                "WHEN receiver = ? THEN sender " +
                "END AS person " +
                "FROM messages WHERE sender = ? OR receiver = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, user);
            statement.setString(2, user);
            statement.setString(3, user);
            statement.setString(4, user);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String person = rs.getString("person");
                if (person != null && !person.equals(user)) {
                    persons.add(person);
                }
            }

        } catch (SQLException e) {
            System.out.println("Fejl i loadYourMessages: " + e.getMessage());
        }

        return persons;
    }

    // ____________________________________________________

    public ResultSet getMessages(String user1, String user2) {
        String query = "SELECT * FROM messages WHERE " +
                "(sender = ? AND receiver = ?) OR " +
                "(sender = ? AND receiver = ?) " +
                "ORDER BY timestamp ASC";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user1);
            statement.setString(2, user2);
            statement.setString(3, user2);
            statement.setString(4, user1);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching messages: " + e.getMessage());
        }
        return null;
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
            con = getConnection(url);
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

    // ____________________________________________________

    public int getStudentID(String username) {
        int id = -1; // default value if not found
        String query = "SELECT student_id FROM users WHERE username = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("student_id"); // parse the column to int
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    // ____________________________________________________

    public String getUserName(int ID) {
        String username = ""; // default value if not found
        String query = "SELECT username FROM users WHERE id = ?";

        try (PreparedStatement getUserName = con.prepareStatement(query)) {
            getUserName.setInt(1, ID);
            ResultSet rs = getUserName.executeQuery();
            if (rs.next()) {
                username = rs.getString("username"); // parse the column to int
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return username;
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
            userData.add(rs.getString("lastonline"));
            userData.add(rs.getString("city"));
            userData.add(rs.getString("abtmeheader"));
            userData.add(rs.getString("abtmedesc"));
            userData.add(rs.getString("abtmefunfacts"));
            userData.add(rs.getString("phone"));
            userData.add(rs.getString("rating"));
            userData.add(rs.getString("social"));
            userData.add(rs.getString("contactheader"));
            userData.add(rs.getString("contactdesc"));

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

    // ____________________________________________________

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

    // ____________________________________________________

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
                        "INSERT INTO hair_type_id (texture, color, length, gender) VALUES (?, ?, ?, ?)",
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

    // ____________________________________________________

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

    // ____________________________________________________

    // Run this once from main-initialize to insert customer hairtype/gender in database.
    public void hairTypeInserter() {

        //edit here
        String[] textures = {"Straight", "Wavy", "Curly", "Coily", "All"};
        String[] colors = {"Blonde", "Black", "Brown", "Red", "Grey", "All"};
        String[] lengths = {"Bald", "Buzz", "Short", "Medium", "Long", "Very Long", "Tied", "All"};
        String[] genders = {"Male", "Female", "All"};

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

    // ____________________________________________________

    public void createBooking(String date, String time, String address, int hairtype_id, boolean exam, int student_id, String paid, String accepted, String customer) {
        String sql = "INSERT INTO bookings (date, time, address, hairtypeId, exam, student_id, paid, accepted, customer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setString(2, time);
            ps.setString(3, address);
            ps.setInt(4, hairtype_id);
            ps.setInt(5, exam ? 1 : 0);
            ps.setInt(6, student_id);
            ps.setString(7, paid);
            ps.setString(8, accepted);
            ps.setString(9, customer);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ____________________________________________________

    public List<BookingCard> getBookings(boolean isExam) {

        List <BookingCard> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE exam = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, isExam);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                String time = rs.getString("time");
                String address = rs.getString("address");
                int hairtypeId = rs.getInt("hairtypeId");
                int studentId = rs.getInt("student_id");
                String paid = rs.getString("paid");
                String accepted = rs.getString("accepted");
                String customer = rs.getString("customer");

                BookingCard booking = new BookingCard(date, time, address, hairtypeId, isExam, studentId, paid, accepted, customer);
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
                String date = rs.getString("date");
                String time = rs.getString("time");
                String address = rs.getString("address");
                int hairtypeId = rs.getInt("hairtypeId");
                String paid = rs.getString("paid");
                String accepted = rs.getString("accepted");
                String customer = rs.getString("customer");

                BookingCard booking = new BookingCard(date, time, address, hairtypeId, isExam, studentId, paid, accepted, customer);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // ____________________________________________________

    public boolean saveTip(String sender, String receiver, double amount, String comment) {
        String query = "INSERT INTO tip (sender, receiver, amount, comment) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setDouble(3, amount);
            stmt.setString(4, comment);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // true if insert succeeded
        } catch (SQLException e) {
            System.out.println("Error saving tip: " + e.getMessage());
            return false;
        }
    }

    // ____________________________________________________

    public List<Tip> getTipsForUser(String username) {

        List<Tip> tips = new ArrayList<>();
        String query = "SELECT sender, receiver, amount, comment FROM tip WHERE receiver = ? ORDER BY id DESC"; // recent first

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tip tip = new Tip(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getDouble("amount"),
                        rs.getString("comment")
                );
                tips.add(tip);
            }
        } catch (SQLException e) {
            System.out.println("Error loading tips: " + e.getMessage());
        }

        return tips;

    }

    // ____________________________________________________

    public boolean updateBookingAcceptedStatus(int requestId, String acceptedStatus) {
        String sql = "UPDATE bookings SET accepted = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, acceptedStatus);
            ps.setInt(2, requestId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ____________________________________________________

    public List<BookingCard> getBookingCardsForUserWithAcceptedStatus(int studentId, String acceptedStatus) {
        List<BookingCard> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE student_id = ? AND accepted = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, acceptedStatus);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String date = rs.getString("date");
                String time = rs.getString("time");
                String address = rs.getString("address");
                int hairtype_id = rs.getInt("hairtypeId");
                boolean exam = rs.getBoolean("exam");
                String paid = rs.getString("paid");
                String accepted = rs.getString("accepted");
                String customer = rs.getString("customer");

                BookingCard booking = new BookingCard(date, time, address, hairtype_id, exam, studentId, paid, accepted, customer);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // ____________________________________________________

    public boolean isBookingDenied(int bookingId) {
        String sql = "SELECT accepted FROM bookings WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String acceptedStatus = rs.getString("accepted");
                return "Denied".equalsIgnoreCase(acceptedStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ____________________________________________________

    public List<Request> getRequestsForUser(int receiverId) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT id, sender, receiver, comment FROM request WHERE receiver = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, receiverId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Request req = new Request();
                req.setId(rs.getInt("id"));
                req.setSender(rs.getString("sender"));
                req.setReceiver(rs.getInt("receiver"));
                req.setComment(rs.getString("comment"));
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    // ____________________________________________________

    public boolean hasRequests(int userId) {
        String sql = "SELECT COUNT(*) AS requestCount FROM request WHERE receiver = " + userId;
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int count = rs.getInt("requestCount");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ____________________________________________________

    public void saveRequest(String sender, int receiver, String comment) {
        String query = "INSERT INTO request (sender, receiver, comment) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setInt(2, receiver);
            stmt.setString(3, comment);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving request: " + e.getMessage());
        }
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

    // ____________________________________________________

    public boolean updateProfileColors(String username, String profileHex, String bannerHex, String roleHex, String bannerUrl) {
        String query = "UPDATE users SET profilehex = ?, bannerhex = ?, rolehex = ? WHERE username = ?";

        try (PreparedStatement updateColors = con.prepareStatement(query)) {
            updateColors.setString(1, profileHex);
            updateColors.setString(2, bannerHex);
            updateColors.setString(3, roleHex);
            updateColors.setString(4, username);

            int rowsAffected = updateColors.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("updateProfileColors failed ##DEBUG");
            e.printStackTrace();
            return false;
        }



    }

    // ____________________________________________________

    public boolean updateLastOnline(String username, String lastOnline) {

        String query = "UPDATE users SET lastonline = ? WHERE username = ?";

        try (PreparedStatement changeLastOnline = con.prepareStatement(query)) {
            changeLastOnline.setString(1, lastOnline);
            changeLastOnline.setString(2, username);
            int rowsAffected = changeLastOnline.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("changeLastOnline failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateStatus(String username, String status) {

        String query = "UPDATE users SET status = ? WHERE username = ?";

        try (PreparedStatement updateStatus = con.prepareStatement(query)) {
            updateStatus.setString(1, status);
            updateStatus.setString(2, username);
            int rowsAffected = updateStatus.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateStatus failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean setCity(String username, String city) {

        String query = "UPDATE users SET city = ? WHERE username = ?";

        try (PreparedStatement setCity = con.prepareStatement(query)) {
            setCity.setString(1, city);
            setCity.setString(2, username);
            int rowsAffected = setCity.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("setCity failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateABTMeHeader(String username, String header) {

        String query = "UPDATE users SET abtmeheader = ? WHERE username = ?";

        try (PreparedStatement updateABTMeHeader = con.prepareStatement(query)) {
            updateABTMeHeader.setString(1, header);
            updateABTMeHeader.setString(2, username);
            int rowsAffected = updateABTMeHeader.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateABTMeHeader failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateABTMeDesc(String username, String desc) {

        String query = "UPDATE users SET abtmedesc = ? WHERE username = ?";

        try (PreparedStatement updateABTMeDesc = con.prepareStatement(query)) {
            updateABTMeDesc.setString(1, desc);
            updateABTMeDesc.setString(2, username);
            int rowsAffected = updateABTMeDesc.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateABTMeDesc failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateABTMeFunFact(String username, String funFact) {

        String query = "UPDATE users SET abtmefunfacts = ? WHERE username = ?";

        try (PreparedStatement updateABTMeFunFact = con.prepareStatement(query)) {
            updateABTMeFunFact.setString(1, funFact);
            updateABTMeFunFact.setString(2, username);
            int rowsAffected = updateABTMeFunFact.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateABTMeFunFact failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updatePhoneNumber(String username, int number) {

        String query = "UPDATE users SET phone = ? WHERE username = ?";

        try (PreparedStatement updatePhoneNumber = con.prepareStatement(query)) {
            updatePhoneNumber.setInt(1, number);
            updatePhoneNumber.setString(2, username);
            int rowsAffected = updatePhoneNumber.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updatePhoneNumber failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateRating(String username, double rating) {

        String query = "UPDATE users SET rating = ? WHERE username = ?";

        try (PreparedStatement updateRating = con.prepareStatement(query)) {
            updateRating.setDouble(1, rating);
            updateRating.setString(2, username);
            int rowsAffected = updateRating.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateRating failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateSocial(String username, String social) {

        String query = "UPDATE users SET social = ? WHERE username = ?";

        try (PreparedStatement updateSocial = con.prepareStatement(query)) {
            updateSocial.setString(1, social);
            updateSocial.setString(2, username);
            int rowsAffected = updateSocial.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateSocial failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateContactHeader(String username, String header) {

        String query = "UPDATE users SET contactheader = ? WHERE username = ?";

        try (PreparedStatement updateContactHeader = con.prepareStatement(query)) {
            updateContactHeader.setString(1, header);
            updateContactHeader.setString(2, username);
            int rowsAffected = updateContactHeader.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateContactHeader failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateContactDesc(String username, String desc) {

        String query = "UPDATE users SET contactdesc = ? WHERE username = ?";

        try (PreparedStatement updateContactDesc = con.prepareStatement(query)) {
            updateContactDesc.setString(1, desc);
            updateContactDesc.setString(2, username);
            int rowsAffected = updateContactDesc.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("updateContactDesc failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean setEmail(String username, String email) {

        String query = "UPDATE users SET email = ? WHERE username = ?";

        try (PreparedStatement setEmail = con.prepareStatement(query)) {
            setEmail.setString(1, email);
            setEmail.setString(2, username);
            int rowsAffected = setEmail.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("setEmail failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean setProfilePicture(String username, String link) {

        String query = "UPDATE users SET profile_picture = ? WHERE username = ?";

        try (PreparedStatement setProfilePicture = con.prepareStatement(query)) {
            setProfilePicture.setString(1, link);
            setProfilePicture.setString(2, username);
            int rowsAffected = setProfilePicture.executeUpdate();

            return rowsAffected > 0; // true

        } catch (SQLException e) {
            System.out.println("setProfilePicture failed!!!");
            return false;
        }

    }

    // ____________________________________________________

    public boolean updateInterests(String username, String link) {
        int userId = getStudentID(username);
        if (userId == -1) return false;

        Set<String> newInterests = Arrays.stream(link.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        String fetchQuery = "SELECT name FROM interest WHERE id = ?";
        String insertQuery = "INSERT INTO interest (id, name) VALUES (?, ?)";
        String deleteQuery = "DELETE FROM interest WHERE id = ? AND name = ?";

        try (
                PreparedStatement fetchStmt = con.prepareStatement(fetchQuery);
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)
        ) {
            fetchStmt.setInt(1, userId);
            ResultSet rs = fetchStmt.executeQuery();

            Set<String> existingInterests = new HashSet<>();
            while (rs.next()) {
                existingInterests.add(rs.getString("name"));
            }

            for (String interest : newInterests) {
                if (!existingInterests.contains(interest)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, interest);
                    insertStmt.addBatch();
                }
            }

            for (String interest : existingInterests) {
                if (!newInterests.contains(interest)) {
                    deleteStmt.setInt(1, userId);
                    deleteStmt.setString(2, interest);
                    deleteStmt.addBatch();
                }
            }

            insertStmt.executeBatch();
            deleteStmt.executeBatch();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ____________________________________________________

    public ArrayList<String> getInterests(int id) {
        ArrayList<String> interests = new ArrayList<>();
        String query = "SELECT name FROM interest WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                interests.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interests;
    }

    // ____________________________________________________

    public boolean saveToAcceptedBookings(int requestId, String comment, String customerName, int studentId, String status) {

        String query = "INSERT INTO accepted_bookings (id, comments, customer, student, planned) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, requestId);
            stmt.setString(2, comment);
            stmt.setString(3, customerName);
            stmt.setInt(4, studentId);
            stmt.setString(5, status);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ____________________________________________________

    public boolean isBookingAccepted(int requestId) {
        String query = "SELECT accepted FROM bookings WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("accepted");
                return "Yes".equalsIgnoreCase(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ____________________________________________________

    public String getBookingAcceptedStatus(int bookingId) {
        String query = "SELECT accepted FROM bookings WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("accepted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ____________________________________________________

    public boolean acceptRequest(int requestId) {

        if (!connected) {
            System.out.println("Not connected to DB!");
            return false;
        }

        String updateBooking = "UPDATE bookings SET accepted = 'Yes' WHERE id = ?";
        String selectRequest = "SELECT sender, comment, receiver FROM request WHERE id = ?";
        String insertAccepted = "INSERT INTO accepted_bookings (id, comments, customer, student, planned) VALUES (?, ?, ?, ?, ?)";
        String deleteRequest = "DELETE FROM request WHERE id = ?";

        try {
            con.setAutoCommit(false);

            // Update bookings.accepted
            PreparedStatement stmt = con.prepareStatement(updateBooking);
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
            stmt.close();

            // Select request info
            stmt = con.prepareStatement(selectRequest);
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();

            String sender = null;
            String comment = null;
            int receiver = -1;

            if (rs.next()) {
                sender = rs.getString("sender");
                comment = rs.getString("comment");
                receiver = rs.getInt("receiver");
            } else {
                con.rollback();
                rs.close();
                stmt.close();
                return false; // request not found
            }
            rs.close();
            stmt.close();

            // Insert into accepted_bookings
            stmt = con.prepareStatement(insertAccepted);
            stmt.setInt(1, requestId);
            stmt.setString(2, comment);
            stmt.setString(3, sender);
            stmt.setInt(4, receiver);
            stmt.setString(5, "Yes");
            stmt.executeUpdate();
            stmt.close();

            // Delete request
            stmt = con.prepareStatement(deleteRequest);
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
            stmt.close();

            con.commit();
            con.setAutoCommit(true);

            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    // ____________________________________________________

    public boolean cancelAcceptedBooking(int bookingId) {
        String deleteAcceptedBooking = "DELETE FROM accepted_bookings WHERE id = ?";
        String updateBookingStatus = "UPDATE bookings SET accepted = 'Denied' WHERE id = ?";

        try {
            con.setAutoCommit(false);

            PreparedStatement stmt = con.prepareStatement(deleteAcceptedBooking);
            stmt.setInt(1, bookingId);
            int deletedRows = stmt.executeUpdate();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement(updateBookingStatus);
            stmt2.setInt(1, bookingId);
            int updatedRows = stmt2.executeUpdate();
            stmt2.close();

            if (deletedRows == 1 && updatedRows == 1) {
                con.commit();
                con.setAutoCommit(true);
                return true;
            } else {
                con.rollback();
                con.setAutoCommit(true);
                return false;
            }

        } catch (SQLException e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    // ____________________________________________________

    public int getBookingIDByAcceptedBookingId(int acceptedBookingId) {
        String query = "SELECT id FROM bookings WHERE id = (SELECT id FROM accepted_bookings WHERE id = ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, acceptedBookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ____________________________________________________

    public List<AcceptedBooking> getAcceptedBookingsForStudent(int studentId) {
        List<AcceptedBooking> bookings = new ArrayList<>();
        String query = "SELECT ab.id AS accepted_booking_id, b.id AS booking_id, ab.comments, ab.customer, ab.student, ab.planned, b.date, b.time " +
                "FROM accepted_bookings ab " +
                "JOIN bookings b ON ab.id = b.id " +
                "WHERE ab.student = ? AND ab.planned = 'Yes'";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AcceptedBooking booking = new AcceptedBooking(
                        rs.getInt("accepted_booking_id"),
                        rs.getInt("booking_id"),
                        rs.getString("comments"),
                        rs.getString("customer"),
                        rs.getInt("student"),
                        rs.getString("planned"),
                        rs.getString("date"),
                        rs.getString("time")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // ____________________________________________________

    public boolean cancelAcceptedBooking(int acceptedBookingId, int bookingId) {
        String deleteAcceptedBooking = "DELETE FROM accepted_bookings WHERE id = ?";
        String updateBookingStatus = "UPDATE bookings SET accepted = 'Denied' WHERE id = ?";

        try {
            con.setAutoCommit(false);

            try (PreparedStatement deleteStmt = con.prepareStatement(deleteAcceptedBooking)) {
                deleteStmt.setInt(1, acceptedBookingId);
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement updateStmt = con.prepareStatement(updateBookingStatus)) {
                updateStmt.setInt(1, bookingId);
                updateStmt.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

} // DBConnector end