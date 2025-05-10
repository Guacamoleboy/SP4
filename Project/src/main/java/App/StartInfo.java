// Packages
package App;

// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StartInfo extends VBox {

    // Attributes
    private int sceneWidth;
    private int sceneHeight;

    // ____________________________________________________

    public StartInfo(int sceneWidth, int sceneHeight){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.setPrefSize(sceneWidth, sceneHeight);

        VBox loginBox = new VBox(15); // Padding / Margin

        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(150); // Center of 300 (Scene)
        this.getChildren().add(sidePane());

    } // Constructor End

    // ____________________________________________________

    public VBox sidePane() {

        Image logo = new Image(getClass().getResource("/assets/logo/Elevtiden-nobg.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(sceneWidth);
        logoView.setPreserveRatio(true);

        VBox sidePaneBox = new VBox(15); // Padding / Margin
        sidePaneBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        sidePaneBox.setAlignment(Pos.CENTER);

        Label label = new Label(getWelcomeMessage());
        label.getStyleClass().add("welcome-label");
        label.setWrapText(true);

        //Placement micro adjustment
        sidePaneBox.setPadding(new Insets(10, 10, 10, 10)); // top, right, bottom, left

        //Adding to VBox
        sidePaneBox.getChildren().addAll(logoView,label);

        return sidePaneBox;

    } //logo() end

    // ____________________________________________________

    private String getWelcomeMessage() {

        String message = "";

        File welcomeMessage = new File("src/main/java/constants/welcomeMessage.txt");

        try {
            Scanner scanner = new Scanner(welcomeMessage);
            while (scanner.hasNextLine()) {
                message += scanner.nextLine()+ "\n";
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

} //StartInfo Class end
