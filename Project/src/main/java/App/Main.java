/*

    SP4 - ICE School Project

    Author(s):
    Ebou, Andreas, Carl Emil & Jonas

    Comments
    ________

    N/A

*/

// Packages
package App;

// Imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.DBConnector;

import static App.UpdateChecker.checkVersion;
import static App.UpdateChecker.getCurrentVersion;

public class Main extends Application { // Client class

    // Attributes

    // DATATYPE //
    public static boolean upToDate = checkVersion();
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    // OBJECT //
    public static Login login = new Login(600, 600);
    private StartInfo startinfo = new StartInfo(300, 600);
    public static DBConnector db;
    public static String lang = "Español";

    // ____________________________________________________

    @Override
    public void start(Stage app) {
        String version = getCurrentVersion();
        app.setTitle("SP4 Version " + version);

        // Initialize database
        db = new DBConnector();
        db.connect(DB_URL);

        // Hairtype database fix
        // db.hairTypeInserter();

        if (db.connect(DB_URL)) {
            System.out.println("Database connection success");
        } else {
            System.out.println("Failed to connect db, creating new");
            db.createNewDatabase(DB_URL);
            db.connect(DB_URL);
        }
        // Side by side layout JavaFX (left -> Right) // HBox
        HBox mainScene = new HBox(startinfo, login); // Start Scene

        Scene scene = new Scene(mainScene, 900, 600);
        app.setResizable(false); // Doesn't allow changeing of width / height
        app.setScene(scene);

        startinfo.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        startinfo.getStyleClass().add("orange");

        login.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        login.getStyleClass().add("body");

        // Logo for application. It's under resources.
        app.getIcons().add(new Image(getClass().getResource("/assets/logo/logo-128.png").toExternalForm()));

        //closeHandle(app);

        app.show();
    }

    // _______________________MAIN_________________________

    public static void main(String[] args) {
        launch();
    }

    // ____________________________________________________

    /*

        If a user closes the app then it should change their status to "Offline" again.
        This code does exactly that. Only for the logged in user. Else all would become "Offline" if
        anyone closes the app.

    */

    /*public void closeHandle(Stage stage){
        stage.setOnCloseRequest(e -> {
            ProcessData processdata = new ProcessData();
            String username = login.getUsername();

            for (User u : processdata.getUsers()){
                if(u.getUsername().equalsIgnoreCase(username)){
                    u.setStatus("Offline");
                    processdata.saveData();
                    break;
                }
            }

            System.exit(0);
        });
    }*/

    // ____________________________________________________

    public static void loginPage(Stage stage) {
        login.goToLoginPage(stage);
    }

    public static void setLang(String tmplang) {
        lang = tmplang;
    }

} // Class end
