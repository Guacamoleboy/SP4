// Packages
package App;

// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

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

        Rectangle splitter = new Rectangle();
        splitter.setHeight(1);
        splitter.setWidth(sceneWidth * 0.8); // or a fixed width
        splitter.getStyleClass().add("splitter-rect");

        VBox sidePaneBox = new VBox(10);
        sidePaneBox.setPadding(new Insets(10));
        sidePaneBox.setAlignment(Pos.CENTER);
        sidePaneBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        sidePaneBox.getStyleClass().add("start-vbox");

        Image logo = new Image(getClass().getResource("/assets/logo/Elevtiden-logo-only.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(sceneWidth*0.7);
        logoView.setPreserveRatio(true);

        // Welcome msg
        Label label = new Label(getWelcomeMessage());
        label.getStyleClass().add("welcome-label");

        // Slideshow
        Image slideShow = new Image(getClass().getResource(getRandomImg()).toExternalForm());

        Label slogan = new Label("STØT FREMTIDENS FRISØRER - MED GOD SAMVITTIGHED");
        slogan.getStyleClass().add("label-3");

        sidePaneBox.getChildren().addAll(logoView, slogan, label);

        sidePaneBox.getChildren().add(new Rectangle(sceneWidth * 0.8, 1, Color.LIGHTGRAY));
        sidePaneBox.getChildren().get(sidePaneBox.getChildren().size() - 1).getStyleClass().add("splitter-rect");

        return sidePaneBox;

    } //logo() end

    // ____________________________________________________

    public String getRandomImg(){

        int randomValue = (int) (Math.random() * 10) + 1;

        try {
            Thread.sleep(1000);
            int previousPicture = randomValue = (int) (Math.random() * 10) + 1; // random billede mellem 1 og 10 (til og med 10)

            // Prevent picture being shown twice
            if(randomValue == previousPicture) {
                randomValue = (int) (Math.random() * 10) + 1;
            }

        } catch (InterruptedException e) {
            System.out.println("error");
        }

        return "/assets/slideshow/" + randomValue;
    }

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
