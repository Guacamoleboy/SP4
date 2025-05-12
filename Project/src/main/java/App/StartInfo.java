// Packages
package App;

// Imports
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

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
    private ImageView slideShow;
    private int randomValue = 0;

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


        Image slideShowPictures = new Image(getClass().getResource(getRandomImg()).toExternalForm());
        slideShow = new ImageView(slideShowPictures);
        slideShow.setPreserveRatio(true);
        slideShow.setSmooth(true);
        slideShow.setFitHeight(200);
        slideShow.getStyleClass().add("slide-show");
        //slideShow.setStyle("-fx-border-radius: 20px; -fx-background-radius: 20px");
        startSlideshow();

        // Welcome msg
        Label label = new Label(getWelcomeMessage());
        label.getStyleClass().add("welcome-label");

        Label slogan = new Label("STØT FREMTIDENS FRISØRER - MED GOD SAMVITTIGHED");
        slogan.getStyleClass().add("label-3");

        sidePaneBox.getChildren().addAll(logoView, slogan, slideShow, label);

        return sidePaneBox;

    } //logo() end

    // ____________________________________________________

    private void startSlideshow() {
        KeyFrame kf = new KeyFrame(Duration.seconds(3), event -> {
            String imgPath = getRandomImg();
            Image newImage = new Image(getClass().getResource(imgPath).toExternalForm());
            slideShow.setImage(newImage);
        });

        Timeline timeline = new Timeline(kf);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // ____________________________________________________

    private String getRandomImg() {
        randomValue = randomValue < 10 ? randomValue+1 : 1;
        return "/assets/slideshow/" + randomValue + ".png";
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
