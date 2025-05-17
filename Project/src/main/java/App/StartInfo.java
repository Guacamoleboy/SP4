// Packages
package App;

// Imports
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class StartInfo extends VBox {

    // Attributes

    // DATATYPE //
    private int sceneWidth;
    private int sceneHeight;
    private int randomValue = 0;

    // OBJECT //
    private ImageView slideShow;

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

        // Sets bubbles to spawn in the entire width / height of StartInfo
        Pane bubblePane = new Pane();
        bubblePane.setPrefSize(300, 600);

        // TimeLine with a delay for each bubble spawn and fade
        Timeline bubbleSpawner = new Timeline(new KeyFrame(Duration.millis(500), e -> {

            Circle bubble = createRandomBubble(bubblePane.getPrefWidth(), bubblePane.getPrefHeight());
            bubblePane.getChildren().add(bubble);

            // Fade out
            FadeTransition fade = new FadeTransition(Duration.seconds(5), bubble);
            fade.setFromValue(bubble.getOpacity());
            fade.setToValue(0);
            fade.setOnFinished(ev -> bubblePane.getChildren().remove(bubble));
            fade.play();

        }));

        // Spawns the bubbles with a TimeLine
        bubbleSpawner.setCycleCount(Timeline.INDEFINITE);
        bubbleSpawner.play();

        // Adds bubbles to sidePaneBox (StartInfo)
        sidePaneBox.getChildren().add(bubblePane);

        // Returns the sidePaneBox (VBox)
        return sidePaneBox;
    }


    // ____________________________________________________

    private Circle createRandomBubble(double maxWidth, double maxHeight) {

        Random randomSize = new Random();

        // Size
        double radius = 5 + randomSize.nextDouble() * 30;
        double x = randomSize.nextDouble() * (maxWidth - 2 * radius) + radius;
        double y = randomSize.nextDouble() * (maxHeight - 2 * radius) + radius;

        // Opacity for the circle
        double opacity = 0.3 + randomSize.nextDouble() * 0.7;

        // Instantiate the bubble
        Circle bubble = new Circle(x, y, radius);

        // Set color & Opacity for the instantiated bubble
        bubble.setFill(Color.web("rgba(117, 80, 0, 0.2)"));
        bubble.setOpacity(opacity);

        // Return the bubble
        return bubble;
    }

    // ____________________________________________________

    private void startSlideshow() { // NOT NEEDED (?)

        // Timeline to display random image in a given interval

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

    private String getRandomImg() { // NOT NEEDED (?)
        randomValue = randomValue < 10 ? randomValue + 1 : 1;
        return "/assets/slideshow/" + randomValue + ".png";
    }

    // ____________________________________________________

    private String getWelcomeMessage() { // NOT NEEDED (?)

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
