/*
    Tjekker gennem git og compare sql
*/

package App;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import util.*;

public class UpdateChecker {

    // Attributes
    private static TextUI ui = new TextUI("narko");
    private static String rawUrl = "https://raw.githubusercontent.com/Guacamoleboy/SP4/main/Project/src/main/java/constants/version.txt";

    public static String version;

    static {
        try {
            version = fetchVersionFrom();
        } catch (Exception e) {
            System.out.println("Something went wrong fetching remote version: " + e.getMessage());
            version = "0.0.0";
        }
    }

    // ________________________________________________________

    public static boolean checkVersion() {
        initializeVersionTable();

        String currentVersion = getCurrentVersion();

        try {
            String latestVersion = fetchVersionFrom();

            if (!isOutdated(currentVersion, latestVersion)) {
                ui.displayMsg(ui.promptTextColor("red") + "New update is available (" + latestVersion + ")\nYour version: " + currentVersion + " " + ui.promptTextColor("reset"));
                return false;
            } else {
                ui.displayMsg(ui.promptTextColor("green") + "\nYou are up to date (" + latestVersion + ")!" + ui.promptTextColor("reset"));
                return true;
            }

        } catch (Exception e) {
            ui.displayMsg("Error while checking for updates: " + e.getMessage());
        }

        return false;
    }

    // ________________________________________________________

    private static void initializeVersionTable() {
        if (Main.db != null && Main.db.isConnected()) {
            String createVersionTable = "CREATE TABLE IF NOT EXISTS version_info (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "version_number TEXT NOT NULL," +
                    "update_date DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            Main.db.executeUpdate(createVersionTable);

            String checkQuery = "SELECT COUNT(*) as count FROM version_info";
            try {
                ResultSet rs = Main.db.executeQuery(checkQuery);
                if (rs != null && rs.next() && rs.getInt("count") == 0) {
                    String insertDefault = "INSERT INTO version_info (version_number) VALUES ('1.0.0')";
                    Main.db.executeUpdate(insertDefault);
                }
            } catch (SQLException e) {
                System.out.println("Error checking version table: " + e.getMessage());
            }
        } else {
            System.out.println("Database not connected, cannot initialize version table");
        }
    }

    // ________________________________________________________

    public static String fetchVersionFrom() throws Exception {
        // Setup
        URL url = new URL(rawUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try {
            int response = connection.getResponseCode();

            if (response != HttpURLConnection.HTTP_OK) {
                throw new Exception("http request failed: " + response);
            }

            try (Scanner scan = new Scanner(connection.getInputStream())) {
                if (scan.hasNextLine()) {
                    String version = scan.nextLine().trim();

                    if (version.isEmpty()) {
                        throw new Exception("Version file is empty");
                    }

                    return version;
                } else {
                    throw new Exception("No data found in file!");
                }
            }
        } catch (Exception e) {
            ui.displayMsg("Error from url: " + rawUrl);
            throw new Exception("Error getting version: " + e.getMessage());
        } finally {
            connection.disconnect();
        }
    }

    // ________________________________________________________

    private static boolean isOutdated(String current, String latest) {
        return current.equals(latest);
    }

    // ________________________________________________________

    public static String getCurrentVersion() {
        if (Main.db != null && Main.db.isConnected()) {
            String query = "SELECT version_number FROM version_info ORDER BY id DESC LIMIT 1";

            try {
                ResultSet rs = Main.db.executeQuery(query);
                if (rs != null && rs.next()) {
                    return rs.getString("version_number");
                }
            } catch (SQLException e) {
                ui.displayMsg("Error retrieving version from database: " + e.getMessage());
            }
        }


        return "0.0.0";
    }

    // ________________________________________________________

    public static boolean updateVersion(String newVersion) {
        if (Main.db != null && Main.db.isConnected()) {
            String query = "INSERT INTO version_info (version_number) VALUES ('" + newVersion + "')";
            return Main.db.executeUpdate(query);
        }
        return false;
    }
}
