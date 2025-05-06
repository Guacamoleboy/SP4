/*

    Checks for outdated versions for a given branch vs main.
    If the branch currently being worked in is outdated, then the
    branch user will be prompted.

*/

package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import util.*;


// ________________________________________________________

public class UpdateChecker {

    // Attributes
    private static TextUI ui = new TextUI("narko");

    // ________________________________________________________

    public static boolean checkVersion(){

        String currentVersion = getCurrentVersion();

        // Link to the version.txt file under my Github
        String url = "https://raw.githubusercontent.com/Guacamoleboy/SP3/main/Project/src/constants/version.txt";

        try {

            // Gets last version (Public version) which is the main branchs version.txt version.
            String latestVersion = fetchVersionFrom(url);

            // Checks if version is outdated
            if (!isOutdated(currentVersion, latestVersion)) {
                ui.displayMsg(ui.promptTextColor("red") + "New update is available ("+latestVersion+")\nYour version: "+ currentVersion +" " + ui.promptTextColor("reset"));
                return false;
            } else {
                ui.displayMsg(ui.promptTextColor("green") + "\nYou are up to date ("+latestVersion+")!"+ ui.promptTextColor("reset"));
                return true;
            }

        } catch(Exception e) {
            ui.displayMsg("Error while checking for updates:" +e.getMessage());
        } // Try-catch end

        // Default return
        return false;

    }

    // ________________________________________________________

    private static String fetchVersionFrom(String tmpurl) throws Exception {

        // Setup
        URL url = new URL(tmpurl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try {

            int response = connection.getResponseCode();

            if (response != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP request failed" + response);
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

            } // Try (INNER)

        } catch (Exception e) {

            ui.displayMsg("Error fetching version from url: " + tmpurl);
            throw new Exception("Error fetching version: " + " | Dev msg: " +e.getMessage());

        } finally {

            connection.disconnect();

        } // Try-catch (OUTTER)

    }

    // ________________________________________________________

    private static boolean isOutdated(String current, String latest) {

        // Compares the two as Strings instead of double so we can have 0.5.5 etc instead of .5, .6, .7 etc.
        // Usually 1.0 is Alpha release.
        return current.equals(latest);

    }

    // ________________________________________________________

    private static String getCurrentVersion() {

        try {

            // Gets locale version from users src/constants folder
            File file = new File("src/constants/version.txt");
            Scanner scan = new Scanner(file);

            if (scan.hasNextLine()) {
                return scan.nextLine().trim();
            }

        } catch (FileNotFoundException e) {
            ui.displayMsg("No file path found" + " | Dev msg: " + e.getMessage());
        } // Try-catch end

        // Default return
        return "0.0.0";
    }

} // UpdateChecker end
