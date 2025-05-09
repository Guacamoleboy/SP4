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
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static App.UpdateChecker.getCurrentVersion;

public class Main extends Application { // Client class

    // Attributes
    private Login login = new Login(600, 600);
    private StartInfo startinfo = new StartInfo(300, 600);
    private StartBorder startborder = new StartBorder(3);

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
        launch();
    }

    // ____________________________________________________


    /*

        hvis en bruger lukker appen, skal deres status ændres til "Offline".
        denne kode gør præcis det. kun for den bruger der er logget ind.
        ellers ville alle blive "Offline" hvis bare én lukker appen... kaos!

*/

    public void closeHandle(Stage stage){
        stage.setOnCloseRequest(e -> {
            ProcessData processdata = new ProcessData();
            String username = login.getUsername();

            if (username != null && !username.isEmpty()) {
                for (User u : processdata.getUsers()){
                    if(u.getUsername().equalsIgnoreCase(username)){
                        processdata.setStatus(username, "Offline");
                        break;
                    }
                }
            }

            System.exit(0);
        });
    }

} // Class end
