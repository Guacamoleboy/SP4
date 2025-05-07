/*

    SP4 Project

    Author:
    Ebou, Andreas, Carl-Emil & Jonas.

    Plan
    ____

    N/A

    Comments
    ________

    N/A


*/

// Packages
package App;

// Imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application { // Client class

    // Attributes
    private Login login = new Login(600, 600);
    private StartInfo startinfo = new StartInfo(300, 600);
    private StartBorder startborder = new StartBorder(3);

    // ____________________________________________________

    @Override
    public void start(Stage app) {

        app.setTitle("SP4 Version 0.1.0");

        // Side by side layout JavaFX (left -> Right) // HBox
        HBox mainScene = new HBox(startinfo, startborder, login); // Start Scene

        Scene scene = new Scene(mainScene, 900, 600);
        app.setResizable(false); // Don't change width / height
        app.setScene(scene);

        closeHandle(app);

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
    public void closeHandle(Stage stage){

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

    }

} // Class end