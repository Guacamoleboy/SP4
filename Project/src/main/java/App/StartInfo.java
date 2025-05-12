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

import javax.xml.transform.Source;
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

        VBox sidePaneBox = new VBox(10);
        sidePaneBox.setPadding(new Insets(10));
        sidePaneBox.setAlignment(Pos.CENTER);
        sidePaneBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        sidePaneBox.getStyleClass().add("start-vbox");

        Image logo = new Image(getClass().getResource("/assets/logo/Elevtiden-logo-only.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(sceneWidth*0.7);
        logoView.setPreserveRatio(true);

        int randomValue = (int) (Math.random() * 10) + 1;

        Image slideShowPictures = new Image(getClass().getResource(getRandomImg()).toExternalForm());
        ImageView slideShow = new ImageView(slideShowPictures);
        slideShow.setFitWidth(sceneWidth * 0.8);
        slideShow.setFitHeight(sceneHeight);
        slideShow.setStyle("-fx-border-radius: 20px; -fx-background-radius: 20px");
        slideShow.setFitWidth(sceneWidth*0.7);
        slideShow.setPreserveRatio(true);

        // Welcome msg
        Label label = new Label(getWelcomeMessage());
        label.getStyleClass().add("welcome-label");

        Label slogan = new Label("STØT FREMTIDENS FRISØRER - MED GOD SAMVITTIGHED");
        slogan.getStyleClass().add("label-3");

        sidePaneBox.getChildren().addAll(logoView, slogan, slideShow, label);

        return sidePaneBox;

    } //logo() end

    // ____________________________________________________

    public String getRandomImg(){

       slide();
       return null;

    }

    // ____________________________________________________

    public void slide() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(("Stoppede"));
        }

        int randomValue = (int) (Math.random() * 10) + 1;

        String path = "/assets/slideshow/" + randomValue + ".jpg";
        getRandomImg();

    }

    // ____________________________________________________

    public String updateImage(String path){
        return null;
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
